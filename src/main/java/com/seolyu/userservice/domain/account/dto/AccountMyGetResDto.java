package com.seolyu.userservice.domain.account.dto;

import com.seolyu.userservice.security.dto.LoginInfo;
import lombok.Getter;

@Getter
public class AccountMyGetResDto {
    private final String email;
    private final String name;

    public AccountMyGetResDto(LoginInfo loginInfo) {
        this.email = loginInfo.getEmail();
        this.name = loginInfo.getName();
    }
}
