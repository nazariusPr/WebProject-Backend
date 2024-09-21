package com.nazarois.WebProject.service;

public interface EmailService {
  void sendVerificationEmail(String to, String verificationToken);
}
