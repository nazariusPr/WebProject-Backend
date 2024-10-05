package com.nazarois.WebProject.service.impl;

import static com.nazarois.WebProject.util.Convertor.convertBase64ToString;

import com.nazarois.WebProject.client.ImageGeneratorClient;
import com.nazarois.WebProject.dto.image.GenerateImageRequest;
import com.nazarois.WebProject.dto.image.GenerateImageResponse;
import com.nazarois.WebProject.dto.image.ImageDto;
import com.nazarois.WebProject.service.ImageGeneratorService;
import com.nazarois.WebProject.service.ImageStorageService;
import com.nazarois.WebProject.util.ImageUtils;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ImageGeneratorServiceImpl implements ImageGeneratorService {

  private final ImageGeneratorClient imageGeneratorClient;
  private final ImageStorageService imageStorageService;
  private final ImageUtils imageUtils;

  public List<ImageDto> generateImage(GenerateImageRequest request) {
    GenerateImageResponse response = imageGeneratorClient.generateImage(request);
    return uploadImages(response).stream()
        .map(image -> ImageDto.builder().url(imageUtils.buildImageUrl(image)).build())
        .toList();
  }

  private List<String> uploadImages(GenerateImageResponse generateImageResponse) {
    List<byte[]> images =
        generateImageResponse.getData().stream()
            .map(image -> convertBase64ToString(image.getB64Json()))
            .toList();
    return imageStorageService.uploadMultipleImages(images);
  }
}
