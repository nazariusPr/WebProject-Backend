package com.nazarois.WebProject.service.impl;

import static com.nazarois.WebProject.constants.AppConstants.IMAGE_CONTENT_TYPE;
import static com.nazarois.WebProject.constants.ExceptionMessageConstants.FILE_DELETING_ERROR_MESSAGE;
import static com.nazarois.WebProject.constants.ExceptionMessageConstants.FILE_UPLOADING_ERROR_MESSAGE;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.nazarois.WebProject.service.ImageStorageService;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageStorageServiceImpl implements ImageStorageService {
  private final AmazonS3 s3Client;

  @Value("${aws.bucket-name}")
  private String bucketName;

  @Override
  public String uploadImage(byte[] imageBytes) {
    String fileName = getUniqueFileName();

    try (ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes)) {
      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentLength(imageBytes.length);
      metadata.setContentType(IMAGE_CONTENT_TYPE);

      PutObjectRequest putObjectRequest =
          new PutObjectRequest(bucketName, fileName, inputStream, metadata);
      s3Client.putObject(putObjectRequest);
    } catch (IOException e) {
      System.err.println(FILE_UPLOADING_ERROR_MESSAGE);
    }

    return fileName;
  }

  @Override
  public List<String> uploadImage(List<byte[]> imagesBytes) {
    List<String> fileNames = new ArrayList<>();
    for (byte[] imageBytes : imagesBytes) {
      String fileName = uploadImage(imageBytes);
      fileNames.add(fileName);
    }
    return fileNames;
  }

  @Override
  public void deleteImage(String fileName) {
    try {
      DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, fileName);
      s3Client.deleteObject(deleteObjectRequest);
    } catch (Exception e) {
      System.err.println(FILE_DELETING_ERROR_MESSAGE);
    }
  }

  @Override
  public void deleteImage(List<String> fileNames) {
    fileNames.forEach(this::deleteImage);
  }

  private String getUniqueFileName() {
    return "image-" + UUID.randomUUID() + ".png";
  }
}
