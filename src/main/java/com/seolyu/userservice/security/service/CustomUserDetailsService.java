package com.seolyu.userservice.security.service;

import com.seolyu.userservice.common.error.ErrorCode;
import com.seolyu.userservice.domain.account.entity.Account;
import com.seolyu.userservice.domain.account.repository.AccountRepository;
import com.seolyu.userservice.security.dto.UserContext;
import com.seolyu.userservice.security.exception.AuthenticationFailException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(username)
                .orElseThrow(() -> new AuthenticationFailException(ErrorCode.INVALID_ACCOUNT));

        return UserContext.of(account);
    }
}