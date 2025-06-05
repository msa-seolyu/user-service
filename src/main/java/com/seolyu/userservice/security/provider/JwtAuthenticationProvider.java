package com.seolyu.userservice.security.provider;

import com.seolyu.userservice.security.dto.UserContext;
import com.seolyu.userservice.security.jwt.JwtFactory;
import com.seolyu.userservice.security.service.CustomUserDetailsService;
import com.seolyu.userservice.security.token.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtFactory jwtFactory;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getPrincipal();
        UserContext userContext = jwtFactory.decode(token);
        userContext = (UserContext) customUserDetailsService.loadUserByUsername(userContext.getUsername());

        return JwtAuthenticationToken.of(userContext);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
