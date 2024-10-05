package com.nazarois.WebProject.service;

import com.nazarois.WebProject.dto.action.ActionDto;
import com.nazarois.WebProject.dto.action.GenerateActionDto;

public interface ActionService {
  ActionDto generate(GenerateActionDto generateActionDto, String userEmail);
}
