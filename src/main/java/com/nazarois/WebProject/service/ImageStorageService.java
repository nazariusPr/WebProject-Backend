package com.nazarois.WebProject.service;

import java.util.List;

public interface ImageStorageService {
  String uploadImage(byte[] imageBytes);

  List<String> uploadImage(List<byte[]> imagesBytes);

  void deleteImage(String fileName);

  void deleteImage(List<String> fileNames);
}
