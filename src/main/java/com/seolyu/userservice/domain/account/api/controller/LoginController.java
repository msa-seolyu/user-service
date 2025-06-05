package com.seolyu.userservice.domain.account.api.controller;

import com.seolyu.userservice.domain.account.api.dto.LoginPostReqDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "login")
@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class LoginController {

    @Operation(summary = "로그인 요청")
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public void login(@RequestBody LoginPostReqDto dto) {
        // Swagger에서 로그인 요청 API를 호출할 수 있도록 별도의 컨트롤러를 구현함
        // 시큐리티 필터와 연동되는 로직으로 처리가 됨
    }
}
