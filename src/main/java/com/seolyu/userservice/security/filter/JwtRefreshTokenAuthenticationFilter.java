package com.seolyu.userservice.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seolyu.userservice.common.error.ErrorCode;
import com.seolyu.userservice.security.dto.RefreshTokenDto;
import com.seolyu.userservice.security.exception.AuthenticationFailException;
import com.seolyu.userservice.security.token.JwtAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class JwtRefreshTokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public static final String REFRESH_TOKEN_URL = "/accounts/token/refresh";
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    @Builder
    public JwtRefreshTokenAuthenticationFilter(String defaultFilterProcessesUrl,
                                               AuthenticationManager authenticationManager,
                                               AuthenticationSuccessHandler authenticationSuccessHandler,
                                               AuthenticationFailureHandler authenticationFailureHandler) {
        super(defaultFilterProcessesUrl);
        this.setAuthenticationManager(authenticationManager);
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        validatePostHttpMethod(request.getMethod());

        RefreshTokenDto dto = new ObjectMapper().readValue(request.getReader(), RefreshTokenDto.class);
        validateToken(dto.getRefreshToken());

        JwtAuthenticationToken token = JwtAuthenticationToken.of(dto.getRefreshToken());
        return super.getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        this.authenticationSuccessHandler.onAuthenticationSuccess(request, response, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException exception) throws IOException, ServletException {
        this.authenticationFailureHandler.onAuthenticationFailure(request, response, exception);
    }

    private void validatePostHttpMethod(String method) {
        if (!HttpMethod.POST.name().equals(method)) {
            throw new AuthenticationFailException(ErrorCode.METHOD_NOT_ALLOWED);
        }
    }

    private void validateToken(String tokenPayload) {
        if (!StringUtils.hasText(tokenPayload)) {
            throw new AuthenticationFailException(ErrorCode.MISSING_TOKEN);
        }
    }
}
