package com.nazarois.WebProject.service;

import com.nazarois.WebProject.dto.authentication.AuthenticateDto;
import com.nazarois.WebProject.model.Role;
import com.nazarois.WebProject.model.User;

import java.util.Set;
import java.util.UUID;

public interface UserService {
  User create(AuthenticateDto authenticateDto, Set<Role> roles);

  User findUserByEmail(String email);

  User findUserById(UUID id);

  User updateUserVerifiedStatus(String email, boolean status);

  void delete(User user);
  void delete(UUID id);
}
