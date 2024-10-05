package com.nazarois.WebProject.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageUtils {
  @Value("${aws.bucket-name}")
  private String bucketName;

  public String buildImageUrl(String fileName) {
    return String.format("https://%s.s3.amazonaws.com/%s", bucketName, fileName);
  }
}
