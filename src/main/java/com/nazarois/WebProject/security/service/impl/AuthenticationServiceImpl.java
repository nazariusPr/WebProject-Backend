package com.nazarois.WebProject.security.service.impl;

import static com.nazarois.WebProject.constants.AppConstants.AUTH_LINK;
import static com.nazarois.WebProject.constants.AppConstants.REFRESH_TOKEN;
import static com.nazarois.WebProject.constants.ExceptionMessageConstants.INVALID_TOKEN_MESSAGE;
import static com.nazarois.WebProject.constants.ExceptionMessageConstants.USER_NOT_VERIFIED_MESSAGE;

import com.nazarois.WebProject.dto.AuthenticateDto;
import com.nazarois.WebProject.dto.TokenDto;
import com.nazarois.WebProject.exception.InvalidTokenException;
import com.nazarois.WebProject.model.EmailVerificationToken;
import com.nazarois.WebProject.model.Role;
import com.nazarois.WebProject.model.User;
import com.nazarois.WebProject.model.enums.UserRole;
import com.nazarois.WebProject.security.service.AuthenticationService;
import com.nazarois.WebProject.security.service.EmailVerificationService;
import com.nazarois.WebProject.security.service.JwtService;
import com.nazarois.WebProject.security.service.UserCredentialService;
import com.nazarois.WebProject.security.service.UserRoleService;
import com.nazarois.WebProject.service.EmailService;
import com.nazarois.WebProject.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
  private final JwtService jwtService;
  private final UserService userService;
  private final UserCredentialService userCredentialService;
  private final EmailVerificationService emailVerificationService;
  private final UserRoleService roleService;
  private final EmailService emailService;
  private final AuthenticationManager authenticationManager;

  @Value("${security.jwt.access-token-expiration}")
  private Long ACCESS_TOKEN_EXPIRATION;

  @Value("${security.jwt.refresh-token-expiration}")
  private Long REFRESH_TOKEN_EXPIRATION;

  @Override
  public void register(AuthenticateDto request) {
    User user = createUser(request);
    EmailVerificationToken token = this.emailVerificationService.create(user);
    String email = user.getEmail();

    this.emailService.sendVerificationEmail(email, token.getId().toString());
  }

  @Override
  public TokenDto verifyEmail(UUID token, HttpServletResponse response) {
    String email = this.emailVerificationService.validate(token);
    User user = this.userService.updateUserVerifiedStatus(email, true);

    String refreshToken = this.getRefreshToken(user);
    this.setRefreshTokenCookie(response, refreshToken);
    String jwtToken = this.jwtService.generateToken(user, this.ACCESS_TOKEN_EXPIRATION);

    return new TokenDto(jwtToken);
  }

  @Override
  public TokenDto authenticate(AuthenticateDto request, HttpServletResponse response) {
    this.authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

    User user = this.userService.findUserByEmail(request.getEmail());
    if (!user.isVerified()) {
      throw new SecurityException(USER_NOT_VERIFIED_MESSAGE);
    }
    String refreshToken = this.getRefreshToken(user);
    this.setRefreshTokenCookie(response, refreshToken);

    String jwtToken = this.jwtService.generateToken(user, this.ACCESS_TOKEN_EXPIRATION);
    return new TokenDto(jwtToken);
  }

  @Override
  public TokenDto refreshToken(String refreshToken) {
    if (refreshToken.isEmpty() || this.jwtService.isTokenExpired(refreshToken)) {
      throw new InvalidTokenException(INVALID_TOKEN_MESSAGE);
    }
    String email = this.jwtService.extractUsername(refreshToken);
    User user = this.userService.findUserByEmail(email);

    String jwtToken = this.jwtService.generateToken(user, this.ACCESS_TOKEN_EXPIRATION);
    return new TokenDto(jwtToken);
  }

  private User createUser(AuthenticateDto authenticateDto) {
    Set<Role> roles = Set.of(roleService.getRole(UserRole.ROLE_USER));
    User user = userService.create(authenticateDto, roles);
    userCredentialService.create(authenticateDto.getPassword(), user);

    return user;
  }

  private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
    ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN, refreshToken)
            .path(AUTH_LINK)
            .maxAge(Math.toIntExact(this.REFRESH_TOKEN_EXPIRATION / 1000))
            .httpOnly(true)
            .secure(false)
            .sameSite("None")
            .build();

    response.addHeader("Set-Cookie", cookie.toString());
  }

  private String getRefreshToken(User user) {
    return this.jwtService.generateToken(user, this.REFRESH_TOKEN_EXPIRATION);
  }
}
