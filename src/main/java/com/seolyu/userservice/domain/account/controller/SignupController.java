package com.seolyu.userservice.domain.account.controller;

import com.seolyu.userservice.domain.account.dto.SignUpEmailDuplicationPostReqDto;
import com.seolyu.userservice.domain.account.dto.SignUpEmailDuplicationPostResDto;
import com.seolyu.userservice.domain.account.dto.SignupPostReqDto;
import com.seolyu.userservice.domain.account.service.SignupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "signup")
@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class SignupController {
    private final SignupService signupService;

    @Operation(summary = "회원가입 요청")
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@RequestBody @Valid SignupPostReqDto dto) {
        signupService.create(dto);
    }

    @Operation(summary = "이메일 중복 체크")
    @PostMapping("/check-email-duplication")
    @ResponseStatus(HttpStatus.OK)
    public SignUpEmailDuplicationPostResDto signup(@RequestBody @Valid SignUpEmailDuplicationPostReqDto dto) {
        boolean isDuplication = signupService.isEmailDuplication(dto.getEmail());
        return new SignUpEmailDuplicationPostResDto(isDuplication);
    }
}
