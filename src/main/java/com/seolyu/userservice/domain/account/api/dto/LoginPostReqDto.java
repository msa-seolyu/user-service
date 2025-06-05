package com.seolyu.userservice.domain.account.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginPostReqDto {
    private String email;
    private String password;
}
