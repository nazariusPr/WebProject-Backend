package com.nazarois.WebProject.service.impl;

import static com.nazarois.WebProject.util.Convertor.convertBase64ToString;

import com.nazarois.WebProject.client.ImageGeneratorClient;
import com.nazarois.WebProject.dto.action.GenerateActionDto;
import com.nazarois.WebProject.dto.image.GeneratedImagesDto;
import com.nazarois.WebProject.service.ImageGeneratorService;
import com.nazarois.WebProject.service.ImageStorageService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ImageGeneratorServiceImpl implements ImageGeneratorService {

  private final ImageGeneratorClient imageGeneratorClient;
  private final ImageStorageService imageStorageService;

  public List<String> generateImage(GenerateActionDto generateActionDto) {
    GeneratedImagesDto response = imageGeneratorClient.generateImage(generateActionDto);
    return uploadImages(response);
  }

  private List<String> uploadImages(GeneratedImagesDto generatedImagesDto) {
    List<byte[]> images =
        generatedImagesDto.getData().stream()
            .map(image -> convertBase64ToString(image.getB64Json()))
            .toList();
    return imageStorageService.uploadMultipleImages(images);
  }
}
