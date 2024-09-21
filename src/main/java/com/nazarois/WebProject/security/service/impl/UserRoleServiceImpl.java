package com.nazarois.WebProject.security.service.impl;

import static com.nazarois.WebProject.constants.ExceptionMessageConstants.ENTITY_NOT_FOUND_MESSAGE;

import com.nazarois.WebProject.model.Role;
import com.nazarois.WebProject.model.enums.UserRole;
import com.nazarois.WebProject.security.repository.UserRoleRepository;
import com.nazarois.WebProject.security.service.UserRoleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {
  private final UserRoleRepository roleRepository;

  @Override
  public Role getRole(UserRole userRole) {
    return this.roleRepository
        .findByRole(userRole)
        .orElseThrow(
            () -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND_MESSAGE, userRole)));
  }
}
