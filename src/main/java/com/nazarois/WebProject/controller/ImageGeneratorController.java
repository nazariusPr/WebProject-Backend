package com.nazarois.WebProject.controller;

import static com.nazarois.WebProject.constants.AppConstants.IMAGE_LINK;

import com.nazarois.WebProject.dto.image.GenerateImageRequest;
import com.nazarois.WebProject.dto.image.Image;
import com.nazarois.WebProject.service.ImageGeneratorService;
import jakarta.validation.ValidationException;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(IMAGE_LINK)
public class ImageGeneratorController {
  private final ImageGeneratorService imageGeneratorService;

  @PostMapping("/generate")
  public ResponseEntity<List<Image>> generateImage(
      @Validated @RequestBody GenerateImageRequest request, BindingResult result) {
    if (result.hasErrors()) {
      log.error("**/ Bad request to generate image");
      throw new ValidationException(
          Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
    }

    return ResponseEntity.ok(imageGeneratorService.generateImage(request));
  }
}
