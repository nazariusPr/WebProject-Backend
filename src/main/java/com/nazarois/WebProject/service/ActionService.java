package com.nazarois.WebProject.service;

import com.nazarois.WebProject.dto.action.ActionDto;
import com.nazarois.WebProject.dto.action.DetailActionDto;
import com.nazarois.WebProject.dto.action.GenerateActionDto;
import com.nazarois.WebProject.dto.page.PageDto;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface ActionService {
  DetailActionDto read(UUID actionId);

  PageDto<ActionDto> read(String email, Pageable pageable);

  ActionDto generate(GenerateActionDto generateActionDto, String userEmail);

  ActionDto cancel(UUID actionId);
}
