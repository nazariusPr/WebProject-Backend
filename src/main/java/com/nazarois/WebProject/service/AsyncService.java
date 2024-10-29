package com.nazarois.WebProject.service;

import com.nazarois.WebProject.dto.action.ActionRequestDto;
import com.nazarois.WebProject.model.Action;

public interface AsyncService {
  void generate(Action action, ActionRequestDto actionRequestDto);
}
