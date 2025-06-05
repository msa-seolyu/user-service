package com.seolyu.userservice.security.dto;

import com.seolyu.userservice.domain.account.entity.Account;
import lombok.Getter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

@Getter
public class UserContext extends User {
    private LoginInfo loginInfo;

    public UserContext(LoginInfo loginInfo, String password) {
        super(loginInfo.getEmail(), password, AuthorityUtils.NO_AUTHORITIES);
        this.loginInfo = loginInfo;
    }

    public UserContext(String username, String password) {
        super(username, password, AuthorityUtils.NO_AUTHORITIES);

    }

    public static UserContext of(Account account) {
        LoginInfo info = LoginInfo.builder()
                .id(account.getId())
                .name(account.getName())
                .email(account.getEmail())
                .build();

        return new UserContext(info, account.getPassword());
    }

    public static UserContext of(String username, String password) {
        return new UserContext(username, password);
    }
}
