package com.nazarois.WebProject.controller;

import com.nazarois.WebProject.dto.action.ActionDto;
import com.nazarois.WebProject.dto.action.GenerateActionDto;
import com.nazarois.WebProject.dto.image.GenerateImageRequest;
import com.nazarois.WebProject.dto.image.ImageDto;
import com.nazarois.WebProject.service.ActionService;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

import static com.nazarois.WebProject.constants.AppConstants.ACTION_LINK;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(ACTION_LINK)
public class ActionController {
  private final ActionService actionService;

  @PostMapping("/generate-image")
  public ResponseEntity<ActionDto> generateImage(
      @Validated @RequestBody GenerateActionDto request,
      BindingResult result,
      Principal principal) {
    if (result.hasErrors()) {
      log.error("**/ Bad request to generate image");
      throw new ValidationException(
          Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
    }

    return ResponseEntity.ok(actionService.generate(request, principal.getName()));
  }
}
