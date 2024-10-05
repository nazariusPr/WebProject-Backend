package com.nazarois.WebProject.service.impl;

import static com.nazarois.WebProject.constants.ExceptionMessageConstants.USER_NOT_FOUND;
import static com.nazarois.WebProject.constants.ExceptionMessageConstants.WRONG_EMAIL_MESSAGE;

import com.nazarois.WebProject.dto.authentication.AuthenticateDto;
import com.nazarois.WebProject.mapper.AuthenticationMapper;
import com.nazarois.WebProject.model.Role;
import com.nazarois.WebProject.model.User;
import com.nazarois.WebProject.model.enums.UserStatus;
import com.nazarois.WebProject.repository.UserRepository;
import com.nazarois.WebProject.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final AuthenticationMapper authenticationMapper;

  @Override
  public User create(AuthenticateDto authenticateDto, Set<Role> roles) {
    User user = authenticationMapper.RegisterDtoToUser(authenticateDto);
    user.setRoles(roles);
    user.setStatus(UserStatus.OFFLINE);
    return userRepository.save(user);
  }

  @Override
  public User findUserByEmail(String email) {
    return this.userRepository
        .findByEmail(email)
        .orElseThrow(() -> new EntityNotFoundException(WRONG_EMAIL_MESSAGE));
  }

  @Override
  public User findUserById(UUID id) {
    return this.userRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
  }

  @Override
  public User updateUserVerifiedStatus(String email, boolean status) {
    User user = this.findUserByEmail(email);
    user.setVerified(status);

    return this.userRepository.save(user);
  }

  @Override
  public void delete(User user) {
    userRepository.delete(user);
  }

  @Override
  public void delete(UUID id) {
    findUserById(id);
    userRepository.deleteById(id);
  }
}
