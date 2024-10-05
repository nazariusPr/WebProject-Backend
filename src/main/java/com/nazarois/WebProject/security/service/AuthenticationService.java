package com.nazarois.WebProject.security.service;

import com.nazarois.WebProject.dto.authentication.AuthenticateDto;
import com.nazarois.WebProject.dto.authentication.TokenDto;
import jakarta.servlet.http.HttpServletResponse;

import java.util.UUID;

public interface AuthenticationService {
  void register(AuthenticateDto request);

  TokenDto verifyEmail(UUID token, HttpServletResponse response);

  void resendVerificationEmail(String email);

  TokenDto authenticate(AuthenticateDto request, HttpServletResponse response);

  TokenDto refreshToken(String refreshToken);

  void setRefreshTokenCookie(HttpServletResponse response, String refreshToken, Long maxAge);
}
