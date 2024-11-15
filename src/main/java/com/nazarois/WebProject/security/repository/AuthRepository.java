package com.nazarois.WebProject.security.repository;

import com.nazarois.WebProject.model.Auth;
import com.nazarois.WebProject.model.enums.AuthType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<Auth, UUID> {
  @Query("SELECT a FROM Auth a WHERE a.authType = :authType")
  Optional<Auth> findByAuthType(AuthType authType);
}
