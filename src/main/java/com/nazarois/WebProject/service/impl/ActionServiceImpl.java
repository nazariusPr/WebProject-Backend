package com.nazarois.WebProject.service.impl;

import static com.nazarois.WebProject.constants.ExceptionMessageConstants.ACTION_CANCELLATION_BAD_REQUEST_MESSAGE;
import static com.nazarois.WebProject.constants.ExceptionMessageConstants.ENTITY_NOT_FOUND_MESSAGE;

import com.nazarois.WebProject.dto.action.ActionDto;
import com.nazarois.WebProject.dto.action.GenerateActionDto;
import com.nazarois.WebProject.exception.exceptions.BadRequestException;
import com.nazarois.WebProject.mapper.ActionMapper;
import com.nazarois.WebProject.model.Action;
import com.nazarois.WebProject.model.enums.ActionStatus;
import com.nazarois.WebProject.model.enums.ActionType;
import com.nazarois.WebProject.repository.ActionRepository;
import com.nazarois.WebProject.service.ActionService;
import com.nazarois.WebProject.service.AsyncService;
import com.nazarois.WebProject.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ActionServiceImpl implements ActionService {
  private final UserService userService;
  private final AsyncService asyncService;
  private final ActionRepository repository;
  private final ActionMapper mapper;

  @Override
  public ActionDto generate(GenerateActionDto generateActionDto, String email) {
    Action action = repository.save(buildInitialGenerateAction(email));
    asyncService.generate(action, generateActionDto);

    return mapper.actionToActionDto(action);
  }

  @Override
  public ActionDto cancel(UUID actionId) {
    Action action = findById(actionId);
    if (action.getActionStatus().equals(ActionStatus.FINISHED)
        || action.getActionStatus().equals(ActionStatus.CANCELLED)) {
      throw new BadRequestException(ACTION_CANCELLATION_BAD_REQUEST_MESSAGE);
    }

    asyncService.cancelTask(actionId);

    action.setActionStatus(ActionStatus.CANCELLED);
    return mapper.actionToActionDto(repository.save(action));
  }

  private Action findById(UUID actionId) {
    return repository
        .findById(actionId)
        .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND_MESSAGE));
  }

  private Action buildInitialGenerateAction(String email) {
    return Action.builder()
        .actionType(ActionType.GENERATED)
        .actionStatus(ActionStatus.INPROGRESS)
        .user(userService.findUserByEmail(email))
        .build();
  }
}
