package com.nazarois.WebProject.validator;

import static com.nazarois.WebProject.constants.AppConstants.LARGE_IMAGE_SIZE;
import static com.nazarois.WebProject.constants.AppConstants.MEDIUM_IMAGE_SIZE;
import static com.nazarois.WebProject.constants.AppConstants.SMALL_IMAGE_SIZE;

import com.nazarois.WebProject.annotation.ValidSize;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class SizeValidator implements ConstraintValidator<ValidSize, String> {

  private final List<String> validSizes =
      Arrays.asList(SMALL_IMAGE_SIZE, MEDIUM_IMAGE_SIZE, LARGE_IMAGE_SIZE);

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    return validSizes.contains(value);
  }
}
