package com.seolyu.userservice.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seolyu.userservice.security.dto.TokenDto;
import com.seolyu.userservice.security.dto.UserContext;
import com.seolyu.userservice.security.jwt.JwtFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final ObjectMapper objectMapper;
    private final JwtFactory jwtFactory;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        UserContext userContext = (UserContext) authentication.getPrincipal();

        String accessToken = jwtFactory.generateAccessToken(userContext);
        String refreshToken = jwtFactory.generateRefreshToken(userContext);

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), new TokenDto(accessToken, refreshToken));

        log.debug("[user-service] 로그인 성공 loginInfo={}", userContext.getLoginInfo());
    }
}
