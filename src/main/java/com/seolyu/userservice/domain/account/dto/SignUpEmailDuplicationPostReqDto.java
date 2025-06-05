package com.seolyu.userservice.domain.account.dto;

import com.seolyu.userservice.common.validation.ValidationPatterns;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpEmailDuplicationPostReqDto {
    @Pattern(regexp = ValidationPatterns.EMAIL)
    private String email;
}
