package com.seolyu.userservice.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seolyu.userservice.common.error.ErrorCode;
import com.seolyu.userservice.common.validation.ValidationPatterns;
import com.seolyu.userservice.security.dto.LoginDto;
import com.seolyu.userservice.security.exception.AuthenticationFailException;
import com.seolyu.userservice.security.token.LoginAuthenticationToken;
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

import java.io.IOException;
import java.util.regex.Pattern;

public class LoginFilter extends AbstractAuthenticationProcessingFilter {
    public static final String LOGIN_URL = "/accounts/login";
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;

    @Builder
    public LoginFilter(String defaultFilterProcessesUrl,
                       AuthenticationManager authenticationManager,
                       AuthenticationSuccessHandler authenticationSuccessHandler,
                       AuthenticationFailureHandler authenticationFailureHandler) {
        super(defaultFilterProcessesUrl);
        this.setAuthenticationManager(authenticationManager);
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        validatePostHttpMethod(request.getMethod());

        LoginDto dto = new ObjectMapper().readValue(request.getReader(), LoginDto.class);
        validateLoginDto(dto);

        LoginAuthenticationToken token = LoginAuthenticationToken.of(dto.getEmail(), dto.getPassword());
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
                                              AuthenticationException failed) throws IOException, ServletException {
        this.authenticationFailureHandler.onAuthenticationFailure(request, response, failed);
    }

    private void validatePostHttpMethod(String method) {
        if (!HttpMethod.POST.name().equals(method)) {
            throw new AuthenticationFailException(ErrorCode.METHOD_NOT_ALLOWED);
        }
    }

    private void validateLoginDto(LoginDto dto) {
        if (!Pattern.matches(ValidationPatterns.EMAIL, dto.getEmail())) {
            throw new AuthenticationFailException(ErrorCode.INVALID_LOGIN_EMAIL);
        }

        if (!Pattern.matches(ValidationPatterns.PASSWORD, dto.getPassword())) {
            throw new AuthenticationFailException(ErrorCode.INVALID_LOGIN_PASSWORD);
        }
    }
}
