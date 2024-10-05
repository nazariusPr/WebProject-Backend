package com.nazarois.WebProject.client;

import com.nazarois.WebProject.config.ImageGeneratorConfig;
import com.nazarois.WebProject.dto.action.GenerateActionDto;
import com.nazarois.WebProject.dto.image.GeneratedImagesDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    name = "imageGenerator",
    url = "${openai.image-generator.url}",
    configuration = ImageGeneratorConfig.class)
public interface ImageGeneratorClient {

  @PostMapping(value = "/v1/images/generations")
  GeneratedImagesDto generateImage(@RequestBody GenerateActionDto request);
}
