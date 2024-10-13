package com.nazarois.WebProject.service;

import com.nazarois.WebProject.dto.action.GenerateActionDto;
import com.nazarois.WebProject.model.Action;
import java.util.UUID;

public interface AsyncService {
  void generate(Action action, GenerateActionDto generateActionDto);

  void cancelTask(UUID actionId);
}
