package com.nazarois.WebProject.security.repository;

import com.nazarois.WebProject.model.Role;

import java.util.Optional;
import java.util.UUID;

import com.nazarois.WebProject.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<Role, UUID> {
  Optional<Role> findByRole(UserRole userRole);
}
