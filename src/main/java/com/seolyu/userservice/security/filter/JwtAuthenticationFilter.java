package com.seolyu.userservice.security.filter;

import com.seolyu.userservice.common.error.ErrorCode;
import com.seolyu.userservice.security.exception.AuthenticationFailException;
import com.seolyu.userservice.security.jwt.JwtFactory;
import com.seolyu.userservice.security.matcher.FilterSkipMatcher;
import com.seolyu.userservice.security.token.JwtAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final AuthenticationFailureHandler authenticationFailureHandler;

    @Builder
    public JwtAuthenticationFilter(FilterSkipMatcher skipMatcher,
                                   AuthenticationManager authenticationManager,
                                   AuthenticationFailureHandler authenticationFailureHandler) {
        super(skipMatcher, authenticationManager);
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        String tokenPayload = request.getHeader(JwtFactory.JWT_TOKEN_HEADER);
        validateToken(tokenPayload);

        String jwtToken = tokenPayload.replace(JwtFactory.JWT_TOKEN_PREFIX, "");
        JwtAuthenticationToken token = JwtAuthenticationToken.of(jwtToken);
        return super.getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(req, res);
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException exception) throws IOException, ServletException {
        this.authenticationFailureHandler.onAuthenticationFailure(request, response, exception);
    }

    private void validateToken(String tokenPayload) {
        if (!StringUtils.hasText(tokenPayload)) {
            throw new AuthenticationFailException(ErrorCode.MISSING_TOKEN);
        }
    }
}
