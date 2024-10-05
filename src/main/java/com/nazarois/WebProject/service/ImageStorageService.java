package com.nazarois.WebProject.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageStorageService {
  String uploadImage(byte[] imageBytes);
  List<String> uploadMultipleImages(List<byte[]> imagesBytes);

  void deleteImage(String fileName);
}
