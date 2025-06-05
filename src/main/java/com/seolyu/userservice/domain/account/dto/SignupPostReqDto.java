package com.seolyu.userservice.domain.account.dto;

import com.seolyu.userservice.common.validation.ValidationPatterns;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupPostReqDto {
    @Size(min = 2)
    @Size(max = 10)
    private String name;

    @Pattern(regexp = ValidationPatterns.EMAIL)
    private String email;

    @Pattern(regexp = ValidationPatterns.PASSWORD)
    private String password;
}
