package com.seolyu.userservice.security.matcher;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum SkipRequestMatcher {
    HEALTH_CHECK(HttpMethod.GET, "/health-check", "헬스 체크"),

    SWAGGER_UI(HttpMethod.GET, "/swagger-ui/**", "swagger ui"),
    SWAGGER_DOCS(HttpMethod.GET, "/v3/api-docs/**", "swagger api-docs"),

    POST_ACCOUNT_LOGIN(HttpMethod.POST, "/accounts/login", "로그인 요청"),
    POST_ACCOUNT_SIGNUP(HttpMethod.POST, "/accounts/signup", "회원가입 요청"),
    POST_TOKEN_REFRESH(HttpMethod.POST, "/accounts/token/refresh", "Token 재발급"),
    POST_ACCOUNT_CHECK_EMAIL_DUPLICATION(HttpMethod.POST, "/accounts/check-email-duplication", "이메일 중복 체크")

    ;

    private final HttpMethod method;
    private final String url;
    private final String description;

    public static List<RequestMatcher> createSkipRequestMatchers() {
        return Arrays.stream(SkipRequestMatcher.values())
                .map(value -> new AntPathRequestMatcher(
                        value.url,
                        value.method != null ? value.method.name() : null
                ))
                .collect(Collectors.toList());
    }
}
