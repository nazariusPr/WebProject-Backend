package com.nazarois.WebProject.security.service.impl;

import com.nazarois.WebProject.model.User;
import com.nazarois.WebProject.model.UserCredential;
import com.nazarois.WebProject.security.repository.UserCredentialRepository;
import com.nazarois.WebProject.security.service.UserCredentialService;
import com.nazarois.WebProject.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.nazarois.WebProject.constants.ExceptionMessageConstants.PASSWORD_NOT_FOUND;

@Service
@AllArgsConstructor
public class UserCredentialServiceImpl implements UserCredentialService {
  private final UserService userService;
  private final UserCredentialRepository passwordRepository;
  private final PasswordEncoder encoder;

  @Override
  public void create(String password, User user) {
    UserCredential userCredential = new UserCredential();
    userCredential.setPassword(this.encoder.encode(password));
    userCredential.setUser(user);

    this.passwordRepository.save(userCredential);
  }

  @Override
  public UserCredential readByUserEmail(String userEmail) {
    User user = this.userService.findUserByEmail(userEmail);
    return this.passwordRepository
        .findByUserId(user.getId())
        .orElseThrow(() -> new BadCredentialsException(PASSWORD_NOT_FOUND));
  }
}
