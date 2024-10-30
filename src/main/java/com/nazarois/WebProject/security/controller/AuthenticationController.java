package com.nazarois.WebProject.security.controller;

import static com.nazarois.WebProject.constants.AppConstants.AUTH_LINK;

import com.nazarois.WebProject.dto.authentication.AuthenticateDto;
import com.nazarois.WebProject.dto.authentication.TokenDto;
import com.nazarois.WebProject.security.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

  @Operation(summary = "Register a new user")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "User registered successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid request")
  })
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

  @Operation(summary = "Verify email using token")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "User verified successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid token")
  })
  @GetMapping
  public ResponseEntity<TokenDto> verifyEmail(
      @RequestParam UUID token, HttpServletResponse response) {
    log.info("**/ Verify user");
    return ResponseEntity.ok(this.authenticationService.verifyEmail(token, response));
  }

  @Operation(summary = "Resend verification email")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Verification email resent successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid email address")
  })
  @PostMapping("/resend-verification-email")
  public ResponseEntity<String> resendVerificationEmail(@RequestParam String email) {
    this.authenticationService.resendVerificationEmail(email);
    return ResponseEntity.ok("Verification email resent successfully.");
  }

  @Operation(summary = "Authenticate user and get token")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Authentication successful"),
    @ApiResponse(responseCode = "400", description = "Invalid credentials")
  })
  @PostMapping
  public ResponseEntity<TokenDto> authenticate(
      @Validated @RequestBody AuthenticateDto request,
      BindingResult result,
      HttpServletResponse response) {
    if (result.hasErrors()) {
      log.error("**/ Bad request to authenticate user");
      throw new ValidationException("Invalid credentials !");
    }
    log.info("**/ Authenticate user");
    return ResponseEntity.ok(this.authenticationService.authenticate(request, response));
  }

  @Operation(summary = "Refresh authentication token")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid refresh token")
  })
  @GetMapping("/refresh-token")
  public ResponseEntity<TokenDto> refreshToken(
      @CookieValue(name = "refresh_token", defaultValue = "") String refreshToken) {
    TokenDto authResponse = this.authenticationService.refreshToken(refreshToken);
    return ResponseEntity.ok(authResponse);
  }

  @Operation(summary = "Logout user and delete refresh token")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Logout successful"),
    @ApiResponse(responseCode = "400", description = "Invalid refresh token")
  })
  @DeleteMapping("/logout")
  public ResponseEntity<Void> deleteRefreshToken(
      HttpServletResponse response,
      @CookieValue(name = "refresh_token", defaultValue = "") String refreshToken) {
    this.authenticationService.setRefreshTokenCookie(response, refreshToken, 0L);
    log.info("**/ Delete refresh token");
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
