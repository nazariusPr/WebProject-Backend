package com.nazarois.WebProject.service;

import com.nazarois.WebProject.dto.action.ActionDto;
import com.nazarois.WebProject.dto.action.ActionFilterDto;
import com.nazarois.WebProject.dto.action.ActionRequestDto;
import com.nazarois.WebProject.dto.action.DetailActionDto;
import com.nazarois.WebProject.dto.page.PageDto;
import com.nazarois.WebProject.model.Action;
import java.util.UUID;

import com.nazarois.WebProject.model.enums.ActionStatus;
import org.springframework.data.domain.Pageable;

public interface ActionService {
  Action findById(UUID actionId);

  DetailActionDto read(UUID actionId);

  PageDto<ActionDto> read(String email, Pageable pageable);

  ActionStatus readActionStatus(UUID actionId);

  PageDto<ActionDto> read(ActionFilterDto filterDto, String email, Pageable pageable);

  ActionDto generate(ActionRequestDto actionRequestDto, String userEmail);

  ActionDto cancel(UUID actionId);

  ActionDto restart(UUID actionId, String email);
}
