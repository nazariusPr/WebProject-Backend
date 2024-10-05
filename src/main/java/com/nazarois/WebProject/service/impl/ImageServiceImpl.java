package com.nazarois.WebProject.service.impl;

import com.nazarois.WebProject.dto.image.ImageDto;
import com.nazarois.WebProject.mapper.ImageMapper;
import com.nazarois.WebProject.model.Action;
import com.nazarois.WebProject.model.Image;
import com.nazarois.WebProject.repository.ImageRepository;
import com.nazarois.WebProject.service.ImageService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {
  private final ImageRepository repository;
  private final ImageMapper mapper;

  @Override
  public ImageDto create(String fileName, String text, Action action) {
    Image image = buildImage(fileName, text, action);
    return mapper.imageToImageDto(repository.save(image));
  }

  @Override
  public List<ImageDto> create(List<String> fileNames, String text, Action action) {
    List<Image> images =
        fileNames.stream().map(fileName -> buildImage(fileName, text, action)).toList();
    return repository.saveAll(images).stream().map(mapper::imageToImageDto).toList();
  }

  private Image buildImage(String fileName, String text, Action action) {
    return Image.builder().fileName(fileName).text(text).action(action).build();
  }
}
