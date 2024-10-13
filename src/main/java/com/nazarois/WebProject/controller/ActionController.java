package com.nazarois.WebProject.controller;

import static com.nazarois.WebProject.constants.AppConstants.ACTION_LINK;

import com.nazarois.WebProject.dto.action.ActionDto;
import com.nazarois.WebProject.dto.action.DetailActionDto;
import com.nazarois.WebProject.dto.action.ActionRequestDto;
import com.nazarois.WebProject.dto.page.PageDto;
import com.nazarois.WebProject.service.ActionService;
import jakarta.validation.ValidationException;
import java.security.Principal;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(ACTION_LINK)
public class ActionController {
  private final ActionService actionService;

  @GetMapping
  public ResponseEntity<PageDto<ActionDto>> read(Pageable pageable, Principal principal) {
    log.info("**/ Reading all actions of user");
    return ResponseEntity.ok(actionService.read(principal.getName(), pageable));
  }

  @GetMapping("/{actionId}")
  public ResponseEntity<DetailActionDto> read(@PathVariable UUID actionId) {
    log.info("**/ Reading action of user");
    return ResponseEntity.ok(actionService.read(actionId));
  }

  @PostMapping("/generate-image")
  public ResponseEntity<ActionDto> generateImage(
      @Validated @RequestBody ActionRequestDto request, BindingResult result, Principal principal) {
    if (result.hasErrors()) {
      log.error("**/ Bad request to generate image");
      throw new ValidationException(
          Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
    }

    return ResponseEntity.ok(actionService.generate(request, principal.getName()));
  }

  @PatchMapping("/restart/{actionId}")
  public ResponseEntity<ActionDto> restartAction(@PathVariable UUID actionId) {
    log.info("**/ Restarting action");
    return ResponseEntity.ok(actionService.restart(actionId));
  }

  @PatchMapping("/cancel/{actionId}")
  public ResponseEntity<ActionDto> cancelAction(@PathVariable UUID actionId) {
    log.info("**/ Cancelling action");
    return ResponseEntity.ok(actionService.cancel(actionId));
  }
}
