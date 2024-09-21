package com.nazarois.WebProject.security.service.impl;

import com.nazarois.WebProject.model.User;
import com.nazarois.WebProject.security.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
  @Value("${security.secret-key}")
  private String SECRET_KEY;

  @Override
  public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = this.extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  @Override
  public String extractUsername(String token) {
    return this.extractClaims(token, Claims::getSubject);
  }

  @Override
  public String generateToken(Map<String, Object> extraClaims, User user, Long expirationTime) {
    extraClaims.put("roles", this.getAuthorities(user));

    return Jwts.builder()
        .setClaims(extraClaims)
        .setSubject(user.getEmail())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
        .signWith(this.getSignInKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  @Override
  public String generateToken(User user, Long expirationTime) {
    return this.generateToken(new HashMap<>(), user, expirationTime);
  }

  @Override
  public boolean isTokenExpired(String token) {
    return this.extractExpiration(token).before(new Date());
  }

  @Override
  public boolean isTokenValid(String token, User user) {
    final String email = this.extractUsername(token);
    return email.equals(user.getEmail()) && !this.isTokenExpired(token);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(this.getSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(this.SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  private Date extractExpiration(String token) {
    return this.extractClaims(token, Claims::getExpiration);
  }

  private String getAuthorities(User user) {
    return user.getRoles().stream()
        .map(role -> role.getRole().name())
        .collect(Collectors.joining(","));
  }
}
