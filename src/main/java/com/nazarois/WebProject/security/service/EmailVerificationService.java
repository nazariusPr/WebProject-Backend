package com.nazarois.WebProject.security.service;

import com.nazarois.WebProject.model.EmailVerificationToken;
import com.nazarois.WebProject.model.User;

import java.util.UUID;

public interface EmailVerificationService {
    EmailVerificationToken create(User user);

    String validate(UUID tokenId);

    void delete(UUID tokenId);
}
