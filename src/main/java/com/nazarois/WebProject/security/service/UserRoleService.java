package com.nazarois.WebProject.security.service;

import com.nazarois.WebProject.model.Role;
import com.nazarois.WebProject.model.enums.UserRole;

public interface UserRoleService {
  Role getRole(UserRole userRole);
}
