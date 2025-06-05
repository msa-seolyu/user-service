package com.seolyu.userservice.security.token;

import com.seolyu.userservice.security.dto.UserContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {
    public JwtAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public JwtAuthenticationToken(String token) {
        super(token, token.length());
    }

    public static JwtAuthenticationToken of(String token) {
        return new JwtAuthenticationToken(token);
    }

    public static JwtAuthenticationToken of(UserContext userContext) {
        return new JwtAuthenticationToken(userContext, userContext.getPassword(), userContext.getAuthorities());
    }
}
