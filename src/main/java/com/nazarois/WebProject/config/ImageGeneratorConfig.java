package com.nazarois.WebProject.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageGeneratorConfig {

  @Value("${openai.image-generator.api-key}")
  private String apiKey;

  @Bean
  public RequestInterceptor apiKeyInterceptor() {
    return template -> template.header("Authorization", "Bearer " + apiKey);
  }
}
