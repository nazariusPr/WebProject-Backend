package com.nazarois.WebProject.service.impl;

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
import java.util.concurrent.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
        executorService.submit(() -> doGenerateAction(action, generateActionDto));
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

    List<String> generatedImages = imageGeneratorService.generateImage(generateActionDto);
    if (task.isCancelled()) {
      imageStorageService.deleteImage(generatedImages);
      throw new InterruptedException("Action was cancelled");
    }

    List<Image> images =
        imageService.create(generatedImages, generateActionDto.getPrompt(), action);
    for (int i = 0; i < 10_000; ++i) {
      for (int j = 0; j < 10_000; ++j) {
        // simulator of action
      }
    }

    if (task.isCancelled()) {
      imageStorageService.deleteImage(generatedImages);
      imageService.delete(images);
      throw new InterruptedException("Action was cancelled");
    }

    action.setActionStatus(ActionStatus.FINISHED);
    action.setImages(images);
    return mapper.actionToActionDto(repository.save(action));
  }
}
