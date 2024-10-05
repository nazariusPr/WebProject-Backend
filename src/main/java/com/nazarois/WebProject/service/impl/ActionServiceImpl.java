package com.nazarois.WebProject.service.impl;

import com.nazarois.WebProject.dto.action.ActionDto;
import com.nazarois.WebProject.dto.action.GenerateActionDto;
import com.nazarois.WebProject.mapper.ActionMapper;
import com.nazarois.WebProject.model.Action;
import com.nazarois.WebProject.model.Image;
import com.nazarois.WebProject.model.enums.ActionStatus;
import com.nazarois.WebProject.model.enums.ActionType;
import com.nazarois.WebProject.repository.ActionRepository;
import com.nazarois.WebProject.service.ActionService;
import com.nazarois.WebProject.service.ImageGeneratorService;
import com.nazarois.WebProject.service.ImageService;
import com.nazarois.WebProject.service.UserService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ActionServiceImpl implements ActionService {
  private final UserService userService;
  private final ImageService imageService;
  private final ImageGeneratorService imageGeneratorService;
  private final ActionRepository repository;
  private final ActionMapper mapper;

  @Override
  @Transactional
  public ActionDto generate(GenerateActionDto generateActionDto, String email) {
    Action action = repository.save(buildInitialGenerateAction(email));

    List<String> generatedImages = imageGeneratorService.generateImage(generateActionDto);
    List<Image> images =
        imageService.create(generatedImages, generateActionDto.getPrompt(), action);
    action.setActionStatus(ActionStatus.FINISHED);
    action.setImages(images);

    return mapper.actionToActionDto(repository.save(action));
  }

  private Action buildInitialGenerateAction(String email) {
    return Action.builder()
        .actionType(ActionType.GENERATED)
        .actionStatus(ActionStatus.INPROGRESS)
        .user(userService.findUserByEmail(email))
        .build();
  }
}
