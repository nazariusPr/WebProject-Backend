package com.nazarois.WebProject.service;

import com.nazarois.WebProject.dto.Image.GenerateImageRequest;
import com.nazarois.WebProject.dto.Image.GenerateImageResponse;

public interface ImageGeneratorService {
  GenerateImageResponse generateImage(GenerateImageRequest generateImageRequest);
}
