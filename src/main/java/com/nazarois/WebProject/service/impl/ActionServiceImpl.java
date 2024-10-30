package com.nazarois.WebProject.service.impl;

import static com.nazarois.WebProject.constants.AppConstants.ACTION_LIMITATION_NUMBER;
import static com.nazarois.WebProject.constants.ExceptionMessageConstants.ACTION_CANCELLATION_BAD_REQUEST_MESSAGE;
import static com.nazarois.WebProject.constants.ExceptionMessageConstants.ACTION_IS_NOT_CANCELLED_MESSAGE;
import static com.nazarois.WebProject.constants.ExceptionMessageConstants.ACTION_IS_NOT_FINISHED_MESSAGE;
import static com.nazarois.WebProject.constants.ExceptionMessageConstants.ACTION_LIMITATION_MESSAGE;
import static com.nazarois.WebProject.constants.ExceptionMessageConstants.ENTITY_NOT_FOUND_MESSAGE;

import com.nazarois.WebProject.dto.action.ActionDto;
import com.nazarois.WebProject.dto.action.ActionFilterDto;
import com.nazarois.WebProject.dto.action.ActionRequestDto;
import com.nazarois.WebProject.dto.action.DetailActionDto;
import com.nazarois.WebProject.dto.page.PageDto;
import com.nazarois.WebProject.exception.exceptions.BadRequestException;
import com.nazarois.WebProject.exception.exceptions.TooManyRequestsException;
import com.nazarois.WebProject.mapper.ActionMapper;
import com.nazarois.WebProject.model.Action;
import com.nazarois.WebProject.model.ActionRequest;
import com.nazarois.WebProject.model.enums.ActionStatus;
import com.nazarois.WebProject.model.enums.ActionType;
import com.nazarois.WebProject.repository.ActionRepository;
import com.nazarois.WebProject.repository.ActionRequestRepository;
import com.nazarois.WebProject.service.ActionService;
import com.nazarois.WebProject.service.AsyncService;
import com.nazarois.WebProject.service.UserService;
import com.nazarois.WebProject.specification.ActionSpecification;
import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ActionServiceImpl implements ActionService {
  private final UserService userService;
  private final AsyncService asyncService;
  private final ActionRepository repository;
  private final ActionRequestRepository actionRequestRepository;
  private final ActionMapper mapper;

  @Override
  public Action findById(UUID actionId) {
    return repository
        .findById(actionId)
        .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND_MESSAGE));
  }

  @Override
  public DetailActionDto read(UUID actionId) {
    Action action = findById(actionId);
    if (!action.getActionStatus().equals(ActionStatus.FINISHED)) {
      throw new BadRequestException(ACTION_IS_NOT_FINISHED_MESSAGE);
    }
    return mapper.actionToDetailActionDto(action);
  }

  @Override
  public PageDto<ActionDto> read(String email, Pageable pageable) {
    Page<Action> actions = repository.findAllByUserEmail(email, pageable);
    return buildActionDtoPage(actions);
  }

  @Override
  public ActionStatus readActionStatus(UUID actionId) {
    return findById(actionId).getActionStatus();
  }

  @Override
  public PageDto<ActionDto> read(ActionFilterDto filterDto, String email, Pageable pageable) {
    ActionSpecification specification = new ActionSpecification(filterDto, email);
    Page<Action> actions = repository.findAll(specification, pageable);
    return buildActionDtoPage(actions);
  }

  @Override
  public ActionDto generate(ActionRequestDto actionRequestDto, String email) {
    validateActionRequest(email);
    Action action = repository.save(buildInitialGenerateAction(email, actionRequestDto.getTitle()));
    saveActionRequest(action, actionRequestDto);
    asyncService.generate(action, actionRequestDto);

    return mapper.actionToActionDto(action);
  }

  @Override
  public ActionDto cancel(UUID actionId) {
    Action action = findById(actionId);
    if (action.getActionStatus().equals(ActionStatus.FINISHED)
        || action.getActionStatus().equals(ActionStatus.CANCELLED)) {
      throw new BadRequestException(ACTION_CANCELLATION_BAD_REQUEST_MESSAGE);
    }

    action.setActionStatus(ActionStatus.CANCELLED);
    return mapper.actionToActionDto(repository.save(action));
  }

  @Override
  public ActionDto restart(UUID actionId, String email) {
    Action action = findById(actionId);
    if (!action.getActionStatus().equals(ActionStatus.CANCELLED)) {
      throw new BadRequestException(ACTION_IS_NOT_CANCELLED_MESSAGE);
    }
    validateActionRequest(email);

    ActionDto actionDto = null;
    if (action.getActionType().equals(ActionType.GENERATED)) {
      actionDto = restartGenerateAction(action);
    } else {
      // to do
    }
    return actionDto;
  }

  private Action buildInitialGenerateAction(String email, String title) {
    return Action.builder()
        .title(title)
        .actionType(ActionType.GENERATED)
        .actionStatus(ActionStatus.INPROGRESS)
        .user(userService.findUserByEmail(email))
        .build();
  }

  private PageDto<ActionDto> buildActionDtoPage(Page<Action> page) {
    return new PageDto<>(
        page.getContent().stream().map(mapper::actionToActionDto).toList(),
        page.getNumber(),
        page.getTotalElements(),
        page.getTotalPages());
  }

  private void saveActionRequest(Action action, ActionRequestDto actionRequestDto) {
    ActionRequest actionRequest = mapper.actionRequestDtoToActionRequest(actionRequestDto);
    actionRequest.setAction(action);
    actionRequestRepository.save(actionRequest);
  }

  private ActionDto restartGenerateAction(Action action) {
    action.setActionStatus(ActionStatus.INPROGRESS);
    ActionRequestDto actionRequestDto =
        mapper.actionRequestToActionRequestDto(action.getActionRequest());
    asyncService.generate(action, actionRequestDto);

    return mapper.actionToActionDto(repository.save(action));
  }

  private void validateActionRequest(String email) {
    int numOfActionInProgress = repository.countActionsOfStatus(email, ActionStatus.INPROGRESS);

    if (numOfActionInProgress >= ACTION_LIMITATION_NUMBER) {
      throw new TooManyRequestsException(ACTION_LIMITATION_MESSAGE);
    }
  }
}
