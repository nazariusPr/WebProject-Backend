package com.nazarois.WebProject.exception.exceptions;

public class BadRequestException extends RuntimeException {
  public BadRequestException() {}

  public BadRequestException(String message) {
    super(message);
  }
}
