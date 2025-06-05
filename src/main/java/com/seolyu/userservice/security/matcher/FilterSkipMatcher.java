package com.seolyu.userservice.security.matcher;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;

public class FilterSkipMatcher implements RequestMatcher {
    private final OrRequestMatcher orRequestMatcher;

    public FilterSkipMatcher(List<RequestMatcher> antPathRequestMatchers) {
        this.orRequestMatcher = new OrRequestMatcher(antPathRequestMatchers);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return !orRequestMatcher.matches(request);
    }
}
