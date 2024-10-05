package com.nazarois.WebProject.service;

import com.nazarois.WebProject.model.Action;
import com.nazarois.WebProject.model.Image;
import java.util.List;

public interface ImageService {
  Image create(String fileName, String text, Action action);

  List<Image> create(List<String> fileNames, String text, Action action);
}
