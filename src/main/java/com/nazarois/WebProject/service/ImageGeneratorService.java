package com.nazarois.WebProject.service;

import com.nazarois.WebProject.dto.action.ActionRequestDto;
import java.util.List;

public interface ImageGeneratorService {
  List<String> generateImage(ActionRequestDto actionRequestDto);
}
