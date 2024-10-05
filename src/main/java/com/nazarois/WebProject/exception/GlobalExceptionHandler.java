package com.nazarois.WebProject.exception;

import com.nazarois.WebProject.dto.exception.ExceptionResponse;
import com.nazarois.WebProject.exception.exceptions.InvalidTokenException;
import com.nazarois.WebProject.exception.exceptions.TokenExpirationException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@AllArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {
  private ErrorAttributes errorAttributes;

  @ExceptionHandler({
    ValidationException.class,
    IllegalArgumentException.class,
    ConstraintViolationException.class,
    DataIntegrityViolationException.class
  })
  public final ResponseEntity<?> handleBadRequestException(WebRequest request) {
    ExceptionResponse exceptionResponse = new ExceptionResponse(getErrorAttributes(request));

    log.warn(exceptionResponse.getMessage(), exceptionResponse);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
  }

  @ExceptionHandler({
    SecurityException.class,
    TokenExpirationException.class,
    InvalidTokenException.class
  })
  public final ResponseEntity<?> handleForbiddenException(WebRequest request) {
    ExceptionResponse exceptionResponse = new ExceptionResponse(getErrorAttributes(request));

    log.warn(exceptionResponse.getMessage(), exceptionResponse);
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exceptionResponse);
  }

  @ExceptionHandler({EntityNotFoundException.class})
  public final ResponseEntity<?> handleNotFoundException(WebRequest request) {
    ExceptionResponse exceptionResponse = new ExceptionResponse(getErrorAttributes(request));

    log.warn(exceptionResponse.getMessage(), exceptionResponse);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
  }

  private Map<String, Object> getErrorAttributes(WebRequest webRequest) {
    return new HashMap<>(
        errorAttributes.getErrorAttributes(
            webRequest, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE)));
  }
}
