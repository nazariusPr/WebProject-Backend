package com.nazarois.WebProject.service;

import java.util.List;

public interface EmailService {
  void sendVerificationEmail(String to, String verificationToken);

  void sendGeneratedImagesEmail(String to, String actionDescription, List<String> images);
}
