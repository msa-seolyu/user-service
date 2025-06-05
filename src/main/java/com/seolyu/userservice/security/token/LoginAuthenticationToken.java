package com.seolyu.userservice.security.token;

import com.seolyu.userservice.security.dto.UserContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class LoginAuthenticationToken extends UsernamePasswordAuthenticationToken {
    public LoginAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public LoginAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public static LoginAuthenticationToken of(String username, String password) {
        return new LoginAuthenticationToken(username, password);
    }

    public static LoginAuthenticationToken of(UserContext userContext) {
        return new LoginAuthenticationToken(userContext, userContext.getPassword(), userContext.getAuthorities());
    }
}
