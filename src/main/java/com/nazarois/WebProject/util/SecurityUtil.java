package com.nazarois.WebProject.util;

import com.nazarois.WebProject.model.User;
import com.nazarois.WebProject.service.UserService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service("securityUtils")
@AllArgsConstructor
public class SecurityUtil {
  private final UserService userService;

  public static List<GrantedAuthority> getUserAuthorities(User user) {
    return user.getRoles().stream()
        .map(role -> new SimpleGrantedAuthority(role.getRole().name()))
        .collect(Collectors.toList());
  }
}
