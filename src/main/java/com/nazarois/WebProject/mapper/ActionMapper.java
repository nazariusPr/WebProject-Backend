package com.nazarois.WebProject.mapper;

import com.nazarois.WebProject.dto.action.ActionDto;
import com.nazarois.WebProject.dto.action.ActionRequestDto;
import com.nazarois.WebProject.dto.action.DetailActionDto;
import com.nazarois.WebProject.model.Action;
import com.nazarois.WebProject.model.ActionRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {ImageMapper.class})
public interface ActionMapper {
  ActionDto actionToActionDto(Action action);

  @Mapping(target = "actionRequestDto", source = "actionRequest")
  DetailActionDto actionToDetailActionDto(Action action);

  ActionRequestDto actionRequestToActionRequestDto(ActionRequest actionRequest);

  ActionRequest actionRequestDtoToActionRequest(ActionRequestDto actionRequestDto);
}
