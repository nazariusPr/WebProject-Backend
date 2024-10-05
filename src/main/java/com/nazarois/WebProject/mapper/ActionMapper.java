package com.nazarois.WebProject.mapper;

import com.nazarois.WebProject.dto.action.ActionDto;
import com.nazarois.WebProject.model.Action;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {ImageMapper.class})
public interface ActionMapper {
  ActionDto actionToActionDto(Action action);
}
