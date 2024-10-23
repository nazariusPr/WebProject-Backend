package com.nazarois.WebProject.util;

import static com.nazarois.WebProject.constants.ExceptionMessageConstants.ACCESS_IS_DENIED_MESSAGE;

import com.nazarois.WebProject.model.Action;
import com.nazarois.WebProject.model.User;
import com.nazarois.WebProject.security.service.JwtService;
import com.nazarois.WebProject.service.ActionService;
import com.nazarois.WebProject.service.UserService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service("securityUtils")
@AllArgsConstructor
public class SecurityUtils {
  private final UserService userService;
  private final ActionService actionService;
  private final JwtService jwtService;

  private UUID currentUserID(Authentication authentication) {
    if (authentication != null && authentication.getPrincipal() instanceof String) {
      return userService.findUserByEmail((String) authentication.getPrincipal()).getId();
    }
    throw new SecurityException(ACCESS_IS_DENIED_MESSAGE);
  }

  public boolean hasAccess(UUID actionId, Authentication authentication) {
    Action action = actionService.findById(actionId);
    return action.getUser().getId().equals(currentUserID(authentication));
  }

  public boolean hasAccess(UUID actionId, String accessToken) {
    String email = jwtService.extractUsername(accessToken);
    Action action = actionService.findById(actionId);

    return action.getUser().getEmail().equals(email);
  }

  public static List<GrantedAuthority> getUserAuthorities(User user) {
    return user.getRoles().stream()
        .map(role -> new SimpleGrantedAuthority(role.getRole().name()))
        .collect(Collectors.toList());
  }
}
