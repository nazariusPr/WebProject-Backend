package com.nazarois.WebProject.dto.action;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.nazarois.WebProject.dto.image.ImageDto;
import com.nazarois.WebProject.model.enums.ActionStatus;
import com.nazarois.WebProject.model.enums.ActionType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ActionDto {
  private UUID id;
  private ActionType actionType;
  private ActionStatus actionStatus;
  private LocalDateTime createdAt;
  private String text;
  private List<ImageDto> images;
}
