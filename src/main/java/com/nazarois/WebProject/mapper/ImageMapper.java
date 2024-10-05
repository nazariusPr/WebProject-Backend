package com.nazarois.WebProject.mapper;

import com.nazarois.WebProject.dto.image.ImageDto;
import com.nazarois.WebProject.model.Image;
import com.nazarois.WebProject.util.ImageUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ImageMapper {
  protected ImageUtils imageUtils;

  @Autowired
  public void setImageUtils(ImageUtils imageUtils) {
    this.imageUtils = imageUtils;
  }

  @Mapping(target = "url", source = "fileName", qualifiedByName = "mapFileName")
  public abstract ImageDto imageToImageDto(Image image);

  @Named("mapFileName")
  protected String mapFileName(String fileName) {
    return imageUtils.buildImageUrl(fileName);
  }
}
