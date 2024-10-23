package com.nazarois.WebProject.service.impl;

import com.nazarois.WebProject.model.enums.ActionStatus;
import com.nazarois.WebProject.service.SseService;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class SseServiceImpl implements SseService {
  private final Map<UUID, SseEmitter> emitters = new ConcurrentHashMap<>();

  @Override
  public SseEmitter registerClient(UUID actionId) {
    SseEmitter emitter = new SseEmitter();
    emitters.put(actionId, emitter);

    emitter.onCompletion(() -> emitters.remove(actionId));
    emitter.onTimeout(() -> emitters.remove(actionId));
    return emitter;
  }

  @Override
  public void sendUpdate(UUID actionId, ActionStatus status) {
    SseEmitter emitter = emitters.get(actionId);
    if (emitter != null) {
      try {
        emitter.send(SseEmitter.event().name("actionUpdate").data(status));
      } catch (IOException e) {
        emitters.remove(actionId);
      }
    }
  }

  @Override
  public void completeEmitter(UUID actionId) {
    SseEmitter emitter = emitters.get(actionId);
    if (emitter != null) {
      emitter.complete();
    }
  }
}
