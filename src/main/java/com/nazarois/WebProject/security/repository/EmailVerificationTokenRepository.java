package com.nazarois.WebProject.security.repository;

import com.nazarois.WebProject.model.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmailVerificationTokenRepository
    extends JpaRepository<EmailVerificationToken, UUID> {}
