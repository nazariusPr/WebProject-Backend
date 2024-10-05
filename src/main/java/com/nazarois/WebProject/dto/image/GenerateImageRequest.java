package com.nazarois.WebProject.dto.image;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.nazarois.WebProject.annotation.ValidSize;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.nazarois.WebProject.constants.AppConstants.IMAGE_RESPONSE_FORMAT;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GenerateImageRequest {
  @NotBlank private String prompt;

  @ValidSize private String size;

  @Min(1)
  @Max(10)
  private int numImages;

  private final String responseFormat = IMAGE_RESPONSE_FORMAT;
}
