package com.nazarois.WebProject.security.controller;

import static com.nazarois.WebProject.constants.AppConstants.AUTH_LINK;

import com.nazarois.WebProject.dto.AuthenticateDto;
import com.nazarois.WebProject.dto.TokenDto;
import com.nazarois.WebProject.security.service.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ValidationException;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(AUTH_LINK)
public class AuthenticationController {
  private final AuthenticationService authenticationService;

  @PostMapping("/register")
  public ResponseEntity<Void> register(
      @Validated @RequestBody AuthenticateDto request, BindingResult result) {
    if (result.hasErrors()) {
      log.error("**/ Bad request to register new user");
      throw new ValidationException(
          Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
    }

    this.authenticationService.register(request);

    log.info("**/ Register new user");
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @GetMapping
  public ResponseEntity<TokenDto> verifyEmail(
      @RequestParam UUID token, HttpServletResponse response) {
    log.info("**/ Verify user");
    return ResponseEntity.ok(this.authenticationService.verifyEmail(token, response));
  }

  @PostMapping
  public ResponseEntity<TokenDto> authenticate(
      @Validated @RequestBody AuthenticateDto request,
      BindingResult result,
      HttpServletResponse response) {
    if (result.hasErrors()) {

      log.error("**/ Bad request to authenticate user");
      throw new ValidationException(
          Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
    }

    log.info("**/ Authenticate user");
    return ResponseEntity.ok(this.authenticationService.authenticate(request, response));
  }

  @GetMapping("/refresh-token")
  public ResponseEntity<TokenDto> refreshToken(
      @CookieValue(name = "refresh_token", defaultValue = "") String refreshToken) {
    TokenDto authResponse = this.authenticationService.refreshToken(refreshToken);
    return ResponseEntity.ok(authResponse);
  }

  @DeleteMapping("/logout")
  public ResponseEntity<Void> deleteRefreshToken(
      HttpServletResponse response,
      @CookieValue(name = "refresh_token", defaultValue = "") String refreshToken) {
    this.authenticationService.setRefreshTokenCookie(response, refreshToken, 0L);

    log.info("**/ Delete refresh token");
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
