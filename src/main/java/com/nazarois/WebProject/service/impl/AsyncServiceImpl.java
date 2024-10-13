package com.nazarois.WebProject.service.impl;

import static com.nazarois.WebProject.constants.ExceptionMessageConstants.ACTION_CANCELLATION_MESSAGE;

import com.nazarois.WebProject.dto.action.ActionDto;
import com.nazarois.WebProject.dto.action.GenerateActionDto;
import com.nazarois.WebProject.mapper.ActionMapper;
import com.nazarois.WebProject.model.Action;
import com.nazarois.WebProject.model.Image;
import com.nazarois.WebProject.model.enums.ActionStatus;
import com.nazarois.WebProject.repository.ActionRepository;
import com.nazarois.WebProject.service.AsyncService;
import com.nazarois.WebProject.service.ImageGeneratorService;
import com.nazarois.WebProject.service.ImageService;
import com.nazarois.WebProject.service.ImageStorageService;
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
  private final ImageService imageService;
  private final ImageGeneratorService imageGeneratorService;
  private final ImageStorageService imageStorageService;
  private final ActionRepository repository;
  private final ActionMapper mapper;
  private final ExecutorService executorService = Executors.newCachedThreadPool();
  private final ConcurrentHashMap<UUID, Future<ActionDto>> ongoingTasks = new ConcurrentHashMap<>();

  @Override
  public void generate(Action action, GenerateActionDto generateActionDto) {
    Future<ActionDto> futureTask =
        executorService.submit(
            () -> {
              try {
                return doGenerateAction(action, generateActionDto);
              } finally {
                ongoingTasks.remove(action.getId());
              }
            });
    ongoingTasks.put(action.getId(), futureTask);
  }

  @Override
  public void cancelTask(UUID actionId) {
    Future<ActionDto> task = ongoingTasks.get(actionId);
    if (task != null) {
      task.cancel(true);
    }
  }

  private ActionDto doGenerateAction(Action action, GenerateActionDto generateActionDto)
      throws InterruptedException {
    Future<ActionDto> task = ongoingTasks.get(action.getId());

    if (task.isCancelled()) {
      throw new InterruptedException(ACTION_CANCELLATION_MESSAGE);
    }

    List<String> generatedImages = imageGeneratorService.generateImage(generateActionDto);
    simulateTask();

    if (task.isCancelled()) {
      log.info("Cancelling the action");

      imageStorageService.deleteImage(generatedImages);
      throw new InterruptedException(ACTION_CANCELLATION_MESSAGE);
    }

    List<Image> images = imageService.create(generatedImages, action);
    simulateTask();

    if (task.isCancelled()) {
      log.info("Cancelling the action");

      imageStorageService.deleteImage(generatedImages);
      imageService.delete(images);
      throw new InterruptedException(ACTION_CANCELLATION_MESSAGE);
    }

    action.setActionStatus(ActionStatus.FINISHED);
    action.setImages(images);
    return mapper.actionToActionDto(repository.save(action));
  }

  private void simulateTask() {
    for (int i = 0; i < 200_000; ++i) {
      for (int j = 0; j < 200_000; ++j) {
        // simulator of action
      }
    }
  }
}
