package com.nazarois.WebProject.security.repository;

import com.nazarois.WebProject.model.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailVerificationTokenRepository
    extends JpaRepository<EmailVerificationToken, UUID> {

    @Query("SELECT e FROM EmailVerificationToken e WHERE e.user.email = :email")
    Optional<EmailVerificationToken> findByUserEmail(String email);
}
