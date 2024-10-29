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
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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
  private final ConcurrentHashMap<UUID, Future<DetailActionDto>> ongoingTasks =
      new ConcurrentHashMap<>();

  @Override
  public void generate(Action action, ActionRequestDto actionRequestDto) {
    Future<DetailActionDto> futureTask =
        executorService.submit(
            () -> {
              try {
                return doGenerateAction(action, actionRequestDto);
              } finally {
                ongoingTasks.remove(action.getId());
                sseService.completeEmitter(action.getId());
              }
            });
    ongoingTasks.put(action.getId(), futureTask);
  }

  @Override
  public void cancelTask(UUID actionId) {
    Future<DetailActionDto> task = ongoingTasks.get(actionId);
    if (task != null) {
      task.cancel(true);
    }
  }

  private DetailActionDto doGenerateAction(Action action, ActionRequestDto actionRequestDto)
      throws InterruptedException {
    Future<DetailActionDto> task = ongoingTasks.get(action.getId());

    if (task.isCancelled()) {
      throw new InterruptedException(ACTION_CANCELLATION_MESSAGE);
    }

    List<String> generatedImages =
        imageGeneratorService.generateImage(
            mapper.actionRequestDtoToGenerateActionDto(actionRequestDto));
    simulateTask();

    if (task.isCancelled()) {
      log.info("**/ Cancelling the action");

      // imageStorageService.deleteImage(generatedImages);
      throw new InterruptedException(ACTION_CANCELLATION_MESSAGE);
    }

    List<Image> images = imageService.create(generatedImages, action);
    simulateTask();

    if (task.isCancelled()) {
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

  private void simulateTask() {
    for (int i = 0; i < 20_000; ++i) {
      for (int j = 0; j < 20_000; ++j) {
        // simulator of action
      }
    }
  }
}
