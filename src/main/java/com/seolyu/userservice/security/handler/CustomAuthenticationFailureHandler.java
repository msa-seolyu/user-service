package com.seolyu.userservice.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seolyu.userservice.common.error.response.ErrorsResponse;
import com.seolyu.userservice.security.exception.AuthenticationFailException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


@RequiredArgsConstructor
@Component
@Slf4j
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        AuthenticationFailException failException = (AuthenticationFailException) exception;

        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(failException.getStatus().value());
        response.getWriter().write(objectMapper.writeValueAsString(ErrorsResponse.create(failException.getErrorCode())));
    }
}
