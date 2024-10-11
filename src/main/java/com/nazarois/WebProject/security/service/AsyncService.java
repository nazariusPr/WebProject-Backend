package com.nazarois.WebProject.security.service;

import com.nazarois.WebProject.dto.action.ActionDto;
import com.nazarois.WebProject.dto.action.GenerateActionDto;
import com.nazarois.WebProject.model.Action;

import java.util.concurrent.CompletableFuture;

public interface AsyncService {
  CompletableFuture<ActionDto> generate(Action action, GenerateActionDto generateActionDto);
}
