package com.nazarois.WebProject.service;

import com.nazarois.WebProject.dto.action.ActionRequestDto;
import com.nazarois.WebProject.model.Action;
import java.util.UUID;

public interface AsyncService {
  void generate(Action action, ActionRequestDto actionRequestDto);

  void cancelTask(UUID actionId);
}
