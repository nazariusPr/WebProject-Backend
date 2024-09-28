package com.nazarois.WebProject.security.service.impl;

import static com.nazarois.WebProject.constants.ExceptionMessageConstants.INVALID_TOKEN_MESSAGE;

import com.nazarois.WebProject.exception.exceptions.TokenExpirationException;
import com.nazarois.WebProject.model.EmailVerificationToken;
import com.nazarois.WebProject.model.User;
import com.nazarois.WebProject.security.repository.EmailVerificationTokenRepository;
import com.nazarois.WebProject.security.service.EmailVerificationService;
import com.nazarois.WebProject.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {
  private final EmailVerificationTokenRepository repository;
  private final UserService userService;

  @Value("${security.verify-token-expiration}")
  private Long expirationTimeInMillis;

  @Override
  public EmailVerificationToken create(User user) {
    EmailVerificationToken emailVerificationToken = new EmailVerificationToken();
    emailVerificationToken.setUser(user);

    LocalDateTime expirationTime = LocalDateTime.now().plusSeconds(expirationTimeInMillis);
    emailVerificationToken.setExpirationTime(expirationTime);

    return this.repository.save(emailVerificationToken);
  }

  @Override
  public String validate(UUID tokenId) {
    EmailVerificationToken verificationToken = this.find(tokenId);
    LocalDateTime expirationTime = verificationToken.getExpirationTime();

    if (expirationTime.isBefore(LocalDateTime.now())) {
      userService.delete(verificationToken.getUser());
      throw new TokenExpirationException(INVALID_TOKEN_MESSAGE);
    }

    String email = verificationToken.getUser().getEmail();
    this.delete(tokenId);

    return email;
  }

  @Override
  public void delete(UUID tokenId) {
    this.find(tokenId);
    this.repository.deleteById(tokenId);
  }

  private EmailVerificationToken find(UUID tokenId) {
    return this.repository
        .findById(tokenId)
        .orElseThrow(() -> new EntityNotFoundException(INVALID_TOKEN_MESSAGE));
  }
}
