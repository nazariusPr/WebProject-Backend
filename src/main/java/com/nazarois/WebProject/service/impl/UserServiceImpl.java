package com.nazarois.WebProject.service.impl;

import static com.nazarois.WebProject.constants.ExceptionMessageConstants.INVALID_AUTH_TYPE_MESSAGE;
import static com.nazarois.WebProject.constants.ExceptionMessageConstants.USER_ALREADY_EXIST_MESSAGE;
import static com.nazarois.WebProject.constants.ExceptionMessageConstants.USER_NOT_FOUND;
import static com.nazarois.WebProject.constants.ExceptionMessageConstants.WRONG_EMAIL_MESSAGE;

import com.nazarois.WebProject.dto.authentication.AuthenticateDto;
import com.nazarois.WebProject.exception.exceptions.BadRequestException;
import com.nazarois.WebProject.mapper.AuthenticationMapper;
import com.nazarois.WebProject.model.Auth;
import com.nazarois.WebProject.model.Role;
import com.nazarois.WebProject.model.User;
import com.nazarois.WebProject.model.enums.AuthType;
import com.nazarois.WebProject.model.enums.UserStatus;
import com.nazarois.WebProject.repository.UserRepository;
import com.nazarois.WebProject.security.repository.AuthRepository;
import com.nazarois.WebProject.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.security.InvalidParameterException;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final AuthRepository authRepository;
  private final AuthenticationMapper authenticationMapper;

  @Override
  public User create(AuthenticateDto authenticateDto, Set<Role> roles) {
    if (userRepository.existsByEmail(authenticateDto.getEmail())) {
      throw new BadRequestException(USER_ALREADY_EXIST_MESSAGE);
    }

    User user = authenticationMapper.RegisterDtoToUser(authenticateDto);
    user.setRoles(roles);
    user.setStatus(UserStatus.OFFLINE);
    user.setAuth(getByAuthType(AuthType.EMAIL));

    return userRepository.save(user);
  }

  @Override
  public User create(String email, Set<Role> roles) {
    if (userRepository.existsByEmail(email)) {
      throw new BadRequestException(USER_ALREADY_EXIST_MESSAGE);
    }

    User user = new User();
    user.setEmail(email);
    user.setRoles(roles);
    user.setStatus(UserStatus.OFFLINE);
    user.setVerified(true);
    user.setAuth(getByAuthType(AuthType.GOOGLE));

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

  private Auth getByAuthType(AuthType authType) {
    return authRepository
        .findByAuthType(authType)
        .orElseThrow(() -> new InvalidParameterException(INVALID_AUTH_TYPE_MESSAGE));
  }
}
