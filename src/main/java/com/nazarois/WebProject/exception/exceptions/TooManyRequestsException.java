package com.nazarois.WebProject.exception.exceptions;

public class TooManyRequestsException extends RuntimeException {
  public TooManyRequestsException() {}

  public TooManyRequestsException(String message) {
    super(message);
  }
}
