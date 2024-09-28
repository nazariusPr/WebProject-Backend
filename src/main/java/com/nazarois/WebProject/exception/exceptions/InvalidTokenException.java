package com.nazarois.WebProject.exception.exceptions;

public class InvalidTokenException extends RuntimeException {
  public InvalidTokenException() {}

  public InvalidTokenException(String message) {
    super(message);
  }
}
