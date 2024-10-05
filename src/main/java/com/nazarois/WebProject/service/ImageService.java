package com.nazarois.WebProject.service;

import com.nazarois.WebProject.dto.image.ImageDto;
import com.nazarois.WebProject.model.Action;
import java.util.List;

public interface ImageService {
  ImageDto create(String fileName, String text, Action action);

  List<ImageDto> create(List<String> fileNames, String text, Action action);
}
