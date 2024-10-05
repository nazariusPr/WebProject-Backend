package com.nazarois.WebProject.service;

import com.nazarois.WebProject.dto.image.GenerateImageRequest;
import com.nazarois.WebProject.dto.image.ImageDto;

import java.util.List;

public interface ImageGeneratorService {
  List<ImageDto> generateImage(GenerateImageRequest generateImageRequest);
}
