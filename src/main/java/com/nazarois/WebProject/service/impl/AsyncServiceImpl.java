package com.nazarois.WebProject.service.impl;

import static com.nazarois.WebProject.constants.ExceptionMessageConstants.ACTION_CANCELLATION_MESSAGE;

import com.nazarois.WebProject.dto.action.ActionRequestDto;
import com.nazarois.WebProject.dto.action.DetailActionDto;
import com.nazarois.WebProject.mapper.ActionMapper;
import com.nazarois.WebProject.model.Action;
import com.nazarois.WebProject.model.Image;
import com.nazarois.WebProject.model.enums.ActionStatus;
import com.nazarois.WebProject.repository.ActionRepository;
import com.nazarois.WebProject.service.AsyncService;
import com.nazarois.WebProject.service.EmailService;
import com.nazarois.WebProject.service.ImageGeneratorService;
import com.nazarois.WebProject.service.ImageService;
import com.nazarois.WebProject.service.ImageStorageService;
import com.nazarois.WebProject.service.SseService;
import com.nazarois.WebProject.util.ImageUtils;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class AsyncServiceImpl implements AsyncService {
  private final SseService sseService;
  private final ImageService imageService;
  private final ImageGeneratorService imageGeneratorService;
  private final ImageStorageService imageStorageService;
  private final EmailService emailService;
  private final ImageUtils imageUtils;
  private final ActionRepository repository;
  private final ActionMapper mapper;
  private final ExecutorService executorService = Executors.newCachedThreadPool();

  @Override
  public void generate(Action action, ActionRequestDto actionRequestDto) {
    executorService.submit(
        () -> {
          try {
            return doGenerateAction(action, actionRequestDto);
          } finally {
            sseService.completeEmitter(action.getId());
          }
        });
  }

  private DetailActionDto doGenerateAction(Action action, ActionRequestDto actionRequestDto)
      throws InterruptedException {
    UUID actionId = action.getId();
    if (getActionStatus(actionId) == ActionStatus.CANCELLED) {
      throw new InterruptedException(ACTION_CANCELLATION_MESSAGE);
    }

    List<String> generatedImages =
        imageGeneratorService.generateImage(
            mapper.actionRequestDtoToGenerateActionDto(actionRequestDto));
    simulateTask();

    if (getActionStatus(actionId) == ActionStatus.CANCELLED) {
      log.info("**/ Cancelling the action");

      // imageStorageService.deleteImage(generatedImages);
      throw new InterruptedException(ACTION_CANCELLATION_MESSAGE);
    }

    List<Image> images = imageService.create(generatedImages, action);
    simulateTask();

    if (getActionStatus(actionId) == ActionStatus.CANCELLED) {
      log.info("**/ Cancelling the action");

      // imageStorageService.deleteImage(generatedImages);
      imageService.delete(images);
      throw new InterruptedException(ACTION_CANCELLATION_MESSAGE);
    }

    sendEmailOfGeneratedImages(
        action.getUser().getEmail(), actionRequestDto.getPrompt(), generatedImages);
    action.setActionStatus(ActionStatus.FINISHED);
    action.setImages(images);
    Action completedAction = repository.save(action);

    sseService.sendUpdate(action.getId(), ActionStatus.FINISHED);
    return mapper.actionToDetailActionDto(completedAction);
  }

  private void sendEmailOfGeneratedImages(
      String email, String actionDescription, List<String> images) {
    List<String> imagesUrl = images.stream().map(imageUtils::buildImageUrl).toList();
    emailService.sendGeneratedImagesEmail(email, actionDescription, imagesUrl);
  }

  private ActionStatus getActionStatus(UUID actionId) {
    Optional<Action> action = repository.findById(actionId);
    return action.isPresent() ? action.get().getActionStatus() : ActionStatus.CANCELLED;
  }

  private void simulateTask() {
    for (int i = 0; i < 100_000; ++i) {
      for (int j = 0; j < 100_000; ++j) {
        // simulator of action
      }
    }
  }
}
