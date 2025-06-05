package com.seolyu.userservice.security.provider;

import com.seolyu.userservice.common.error.ErrorCode;
import com.seolyu.userservice.security.dto.UserContext;
import com.seolyu.userservice.security.exception.AuthenticationFailException;
import com.seolyu.userservice.security.service.CustomUserDetailsService;
import com.seolyu.userservice.security.token.LoginAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginAuthenticationProvider implements AuthenticationProvider {
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = (String) authentication.getCredentials();

        UserContext userContext = (UserContext) customUserDetailsService.loadUserByUsername(email);

        if(!passwordEncoder.matches(password, userContext.getPassword())) {
            throw new AuthenticationFailException(ErrorCode.INVALID_ACCOUNT);
        }

        return LoginAuthenticationToken.of(userContext);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return LoginAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
