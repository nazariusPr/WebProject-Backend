package com.nazarois.WebProject.security.provider;

import static com.nazarois.WebProject.constants.ExceptionMessageConstants.BAD_CREDENTIALS_MESSAGE;
import static com.nazarois.WebProject.util.SecurityUtils.getUserAuthorities;

import com.nazarois.WebProject.model.User;
import com.nazarois.WebProject.model.UserCredential;
import com.nazarois.WebProject.security.service.UserCredentialService;
import com.nazarois.WebProject.service.UserService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final UserService userService;
    private final UserCredentialService userCredentialService;
    private final PasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        if (!this.validateUserCredentials(email, password)) {
            throw new BadCredentialsException(BAD_CREDENTIALS_MESSAGE);
        }

        return new UsernamePasswordAuthenticationToken(email, "", this.getAuthorities(email));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private boolean validateUserCredentials(String email, String password) {
        UserCredential userCredential = this.userCredentialService.readByUserEmail(email);
        return this.encoder.matches(password, userCredential.getPassword());
    }

    private List<GrantedAuthority> getAuthorities(String email) {
        User user = this.userService.findUserByEmail(email);
        return getUserAuthorities(user);
    }
}
