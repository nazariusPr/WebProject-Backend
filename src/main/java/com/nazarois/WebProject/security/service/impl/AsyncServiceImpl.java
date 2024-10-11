package com.nazarois.WebProject.security.service.impl;

import com.nazarois.WebProject.dto.action.ActionDto;
import com.nazarois.WebProject.dto.action.GenerateActionDto;
import com.nazarois.WebProject.mapper.ActionMapper;
import com.nazarois.WebProject.model.Action;
import com.nazarois.WebProject.model.Image;
import com.nazarois.WebProject.model.enums.ActionStatus;
import com.nazarois.WebProject.repository.ActionRepository;
import com.nazarois.WebProject.security.service.AsyncService;
import com.nazarois.WebProject.service.ImageGeneratorService;
import com.nazarois.WebProject.service.ImageService;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AsyncServiceImpl implements AsyncService {
  private final ImageService imageService;
  private final ImageGeneratorService imageGeneratorService;
  private final ActionRepository repository;
  private final ActionMapper mapper;

  @Async
  @Override
  public CompletableFuture<ActionDto> generate(Action action, GenerateActionDto generateActionDto) {
    List<String> generatedImages = imageGeneratorService.generateImage(generateActionDto);
    List<Image> images =
        imageService.create(generatedImages, generateActionDto.getPrompt(), action);
    action.setActionStatus(ActionStatus.FINISHED);
    action.setImages(images);

    ActionDto dto = mapper.actionToActionDto(repository.save(action));
    return CompletableFuture.completedFuture(dto);
  }
}
