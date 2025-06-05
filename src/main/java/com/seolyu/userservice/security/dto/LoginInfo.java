package com.seolyu.userservice.security.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class LoginInfo {
    private Long id;
    private String email;
    private String name;
}
