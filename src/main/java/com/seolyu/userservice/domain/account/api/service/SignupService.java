package com.seolyu.userservice.domain.account.api.service;

import com.seolyu.userservice.domain.account.api.dto.SignupPostReqDto;
import com.seolyu.userservice.domain.account.domain.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignupService {
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 이메일 중복 체크
     * @param email 사용자 email
     * @return 중복 여부
     */
    public boolean isEmailDuplication(String email) {
        return accountService.isExistsByEmail(email);
    }

    /**
     * 회원 생성
     * @param dto 회원가입을 위한 요청 데이터
     */
    @Transactional
    public void create(SignupPostReqDto dto) {
        // 이메일 중복 확인
        accountService.validateDuplicatedEmail(dto.getEmail());

        // 저장
        Account account = this.toEntity(dto);
        accountService.save(account);
    }

    private Account toEntity(SignupPostReqDto dto) {
        return Account.builder()
                .email(dto.getEmail())
                .password(this.encryptPassword(dto.getPassword()))
                .name(dto.getName())
                .build();
    }

    private String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
