package com.nazarois.WebProject.service;

import com.nazarois.WebProject.model.enums.ActionStatus;
import java.util.UUID;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseService {
  SseEmitter registerClient(UUID actionId);

  void sendUpdate(UUID actionId, ActionStatus status);

  void completeEmitter(UUID actionId);
}
