package com.nazarois.WebProject.security.repository;

import com.nazarois.WebProject.model.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserCredentialRepository extends JpaRepository<UserCredential, UUID> {
  Optional<UserCredential> findByUserId(UUID userId);
}
