package com.seolyu.userservice.security;

import com.seolyu.userservice.config.CorsFilterConfig;
import com.seolyu.userservice.security.filter.JwtAuthenticationFilter;
import com.seolyu.userservice.security.filter.JwtRefreshTokenAuthenticationFilter;
import com.seolyu.userservice.security.filter.LoginFilter;
import com.seolyu.userservice.security.handler.CustomAuthenticationFailureHandler;
import com.seolyu.userservice.security.handler.CustomAuthenticationSuccessHandler;
import com.seolyu.userservice.security.matcher.FilterSkipMatcher;
import com.seolyu.userservice.security.matcher.SkipRequestMatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    private final CorsFilterConfig corsFilterConfig;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final AuthenticationManager authenticationManager;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsFilterConfig.corsConfigurationSource()))
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .headers(headersConfigurer ->
                        headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                )
                .addFilterBefore(loginFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtRefreshTokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * 로그인 요청
     * 로그인 요청 URL, 인증 매니저, 성공/실패 핸들러 설정
     *
     * @return LoginFilter 필터 객체
     */
    private LoginFilter loginFilter() {
        return LoginFilter.builder()
                .defaultFilterProcessesUrl(LoginFilter.LOGIN_URL)
                .authenticationManager(authenticationManager)
                .authenticationSuccessHandler(customAuthenticationSuccessHandler)
                .authenticationFailureHandler(customAuthenticationFailureHandler)
                .build();
    }

    /**
     * JWT 토큰 인증
     * 인증이 스킵될 URL, 인증 매니저, 성공/실패 핸들러 설정
     *
     * @return JwtAuthenticationFilter 필터 객체
     */
    private JwtAuthenticationFilter jwtAuthenticationFilter() {
        List<RequestMatcher> skipRequestMatchers = SkipRequestMatcher.createSkipRequestMatchers();
        FilterSkipMatcher filterSkipMatcher = new FilterSkipMatcher(skipRequestMatchers);

        return JwtAuthenticationFilter.builder()
                .skipMatcher(filterSkipMatcher)
                .authenticationManager(authenticationManager)
                .authenticationFailureHandler(customAuthenticationFailureHandler)
                .build();
    }

    /**
     * JWT Refresh Token 재발급
     * 재발급 요청 URL, 인증 매니저, 성공/실패 핸들러 설정
     *
     * @return JwtRefreshTokenAuthenticationFilter 필터 객체
     */
    private JwtRefreshTokenAuthenticationFilter jwtRefreshTokenAuthenticationFilter() {
        return JwtRefreshTokenAuthenticationFilter.builder()
                .defaultFilterProcessesUrl(JwtRefreshTokenAuthenticationFilter.REFRESH_TOKEN_URL)
                .authenticationManager(authenticationManager)
                .authenticationSuccessHandler(customAuthenticationSuccessHandler)
                .authenticationFailureHandler(customAuthenticationFailureHandler)
                .build();
    }
}

