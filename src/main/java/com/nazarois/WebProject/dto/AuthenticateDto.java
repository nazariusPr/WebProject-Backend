package com.nazarois.WebProject.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AuthenticateDto {
  @NotBlank private String email;

  @Pattern(
      regexp = "[A-Za-z\\d]{6,}",
      message = "Must be minimum 6 symbols long, using digits and latin letters")
  @NotBlank
  private String password;
}
