package com.nazarois.WebProject.service;

import com.nazarois.WebProject.dto.image.GenerateImageRequest;
import com.nazarois.WebProject.dto.image.GenerateImageResponse;
import com.nazarois.WebProject.dto.image.Image;

import java.util.List;

public interface ImageGeneratorService {
  List<Image> generateImage(GenerateImageRequest generateImageRequest);
}
