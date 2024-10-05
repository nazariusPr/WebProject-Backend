package com.nazarois.WebProject.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.nazarois.WebProject.constants.AppConstants.AMAZON_URL;

@Component
@RequiredArgsConstructor
public class ImageUtils {
  @Value("${aws.bucket-name}")
  private String bucketName;

  public String buildImageUrl(String fileName) {
    return String.format(AMAZON_URL, bucketName, fileName);
  }
}
