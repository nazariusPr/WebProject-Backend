package com.nazarois.WebProject.security.service;

import com.nazarois.WebProject.model.User;
import io.jsonwebtoken.Claims;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {
  <T> T extractClaims(String token, Function<Claims, T> claimsResolver);

  public String extractUsername(String token);

  public String generateToken(Map<String, Object> extraClaims, User user, Long expirationTime);

  public String generateToken(User userDetails, Long expirationTime);

  boolean isTokenExpired(String token);

  boolean isTokenValid(String token, User user);
}
