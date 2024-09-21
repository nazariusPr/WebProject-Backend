package com.nazarois.WebProject.security.service;

import com.nazarois.WebProject.dto.AuthenticateDto;
import com.nazarois.WebProject.dto.TokenDto;
import jakarta.servlet.http.HttpServletResponse;

import java.util.UUID;

public interface AuthenticationService {
  void register(AuthenticateDto request);

  TokenDto verifyEmail(UUID token, HttpServletResponse response);

  TokenDto authenticate(AuthenticateDto request, HttpServletResponse response);

  TokenDto refreshToken(String refreshToken);
}
