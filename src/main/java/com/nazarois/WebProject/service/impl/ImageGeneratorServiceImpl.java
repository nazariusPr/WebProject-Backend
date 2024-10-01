package com.nazarois.WebProject.service.impl;

import com.nazarois.WebProject.client.ImageGeneratorClient;
import com.nazarois.WebProject.dto.Image.GenerateImageRequest;
import com.nazarois.WebProject.dto.Image.GenerateImageResponse;
import com.nazarois.WebProject.service.ImageGeneratorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ImageGeneratorServiceImpl implements ImageGeneratorService {

  private final ImageGeneratorClient imageGeneratorClient;

  public GenerateImageResponse generateImage(final GenerateImageRequest request) {
    return imageGeneratorClient.generateImage(request);
  }
}
