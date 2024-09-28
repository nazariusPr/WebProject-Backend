package com.nazarois.WebProject.security.service.impl;

import static com.nazarois.WebProject.constants.AppConstants.AUTH_LINK;
import static com.nazarois.WebProject.constants.AppConstants.REFRESH_TOKEN;
import static com.nazarois.WebProject.constants.AppConstants.REFRESH_TOKEN_CLAIM;
import static com.nazarois.WebProject.constants.ExceptionMessageConstants.INVALID_TOKEN_MESSAGE;
import static com.nazarois.WebProject.constants.ExceptionMessageConstants.USER_NOT_VERIFIED_MESSAGE;

import com.nazarois.WebProject.dto.AuthenticateDto;
import com.nazarois.WebProject.dto.TokenDto;
import com.nazarois.WebProject.exception.exceptions.InvalidTokenException;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  @Value("${security.cookie-expiration}")
  private Long COOKIE_EXPIRATION;

  @Override
  @Transactional
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
    this.setRefreshTokenCookie(response, refreshToken, COOKIE_EXPIRATION);
    String jwtToken = this.jwtService.generateToken(user, this.ACCESS_TOKEN_EXPIRATION);

    return new TokenDto(jwtToken);
  }

  @Override
  public void resendVerificationEmail(String email) {
    EmailVerificationToken token = this.emailVerificationService.findByUserEmail(email);
    this.emailService.sendVerificationEmail(email, token.getId().toString());
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
    this.setRefreshTokenCookie(response, refreshToken, COOKIE_EXPIRATION);

    String jwtToken = this.jwtService.generateToken(user, this.ACCESS_TOKEN_EXPIRATION);
    return new TokenDto(jwtToken);
  }

  @Override
  public TokenDto refreshToken(String refreshToken) {
    validateRefreshToken(refreshToken);

    String email = this.jwtService.extractUsername(refreshToken);
    User user = this.userService.findUserByEmail(email);

    String jwtToken = this.jwtService.generateToken(user, this.ACCESS_TOKEN_EXPIRATION);
    return new TokenDto(jwtToken);
  }

  @Override
  public void setRefreshTokenCookie(
      HttpServletResponse response, String refreshToken, Long maxAge) {
    ResponseCookie cookie =
        ResponseCookie.from(REFRESH_TOKEN, refreshToken)
            .path(AUTH_LINK)
            .maxAge(maxAge)
            .httpOnly(true)
            .secure(false)
            .sameSite("None")
            .build();

    response.addHeader("Set-Cookie", cookie.toString());
  }

  private User createUser(AuthenticateDto authenticateDto) {
    Set<Role> roles = Set.of(roleService.getRole(UserRole.ROLE_USER));
    User user = userService.create(authenticateDto, roles);
    userCredentialService.create(authenticateDto.getPassword(), user);

    return user;
  }

  private String getRefreshToken(User user) {
    Map<String, Object> claims = new HashMap<>();
    claims.put(REFRESH_TOKEN_CLAIM, true);

    return this.jwtService.generateToken(claims, user, this.REFRESH_TOKEN_EXPIRATION);
  }

  private void validateRefreshToken(String refreshToken) {
    if (refreshToken.isEmpty()
        || this.jwtService.isTokenExpired(refreshToken)
        || !Boolean.TRUE.equals(
            this.jwtService.extractClaims(
                refreshToken, claims -> (Boolean) claims.get(REFRESH_TOKEN_CLAIM)))) {

      throw new InvalidTokenException(INVALID_TOKEN_MESSAGE);
    }
  }
}
