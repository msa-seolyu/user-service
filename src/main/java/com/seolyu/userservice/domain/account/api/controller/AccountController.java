package com.seolyu.userservice.domain.account.api.controller;

import com.seolyu.userservice.security.CurrentUser;
import com.seolyu.userservice.domain.account.api.dto.AccountMyGetResDto;
import com.seolyu.userservice.domain.account.api.dto.RefreshTokenPostReqDto;
import com.seolyu.userservice.security.dto.LoginInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "account")
@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {

    @Operation(summary = "로그인한 사용자 정보 조회")
    @GetMapping("/my")
    @ResponseStatus(HttpStatus.OK)
    public AccountMyGetResDto getMy(@CurrentUser LoginInfo loginInfo) {
        return new AccountMyGetResDto(loginInfo);
    }

    @Operation(summary = "Token 재발급")
    @PostMapping("/token/refresh")
    @ResponseStatus(HttpStatus.OK)
    public void tokenRefresh(@RequestBody RefreshTokenPostReqDto dto) {
        // Swagger에서 토큰 재발급 API를 호출할 수 있도록 별도의 컨트롤러를 구현함
        // 시큐리티 필터와 연동되는 로직으로 처리가 됨
    }
}
