package com.nazarois.WebProject.security.service;

import com.nazarois.WebProject.model.User;
import com.nazarois.WebProject.model.UserCredential;

public interface UserCredentialService {
  void create(String password, User user);

  UserCredential readByUserEmail(String userEmail);
}
