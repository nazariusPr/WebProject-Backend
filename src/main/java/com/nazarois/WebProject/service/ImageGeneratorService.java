package com.nazarois.WebProject.service;

import com.nazarois.WebProject.dto.action.GenerateActionDto;
import java.util.List;

public interface ImageGeneratorService {
  List<String> generateImage(GenerateActionDto generateActionDto);
}
