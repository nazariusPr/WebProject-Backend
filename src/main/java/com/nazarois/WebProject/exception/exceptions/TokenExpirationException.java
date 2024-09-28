package com.nazarois.WebProject.exception.exceptions;

public class TokenExpirationException extends RuntimeException {
  public TokenExpirationException() {}

  public TokenExpirationException(String message) {
    super(message);
  }
}
