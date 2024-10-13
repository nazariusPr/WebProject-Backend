package com.nazarois.WebProject.service;

import com.nazarois.WebProject.dto.action.DetailActionDto;
import com.nazarois.WebProject.dto.action.GenerateActionDto;
import java.util.UUID;

public interface ActionService {
  DetailActionDto generate(GenerateActionDto generateActionDto, String userEmail);
  DetailActionDto cancel(UUID actionId);
}
