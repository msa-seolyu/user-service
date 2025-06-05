package com.seolyu.userservice.domain.account.api.service;

import com.seolyu.userservice.common.error.ErrorCode;
import com.seolyu.userservice.common.error.exception.UserException;
import com.seolyu.userservice.domain.account.domain.entity.Account;
import com.seolyu.userservice.domain.account.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    /**
     * 회원 조회
     * @param id 회원 ID
     * @return 회원 정보
     */
    public Account getAccount(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new UserException(ErrorCode.ACCOUNT_NOT_FOUND));
    }

    /**
     * 회원 저장
     * @param account 회원
     */
    @Transactional
    public void save(Account account) {
        accountRepository.save(account);
    }

    /**
     * 이메일 중복 여부
     * @param email 사용자 이메일
     * @return boolean 중복 여부
     */
    public boolean isExistsByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }

    /**
     * 이메일 중복 유효성 확인
     * @param email 사용자 이메일
     */
    public void validateDuplicatedEmail(String email) {
        boolean isExists = this.isExistsByEmail(email);
        if(isExists) {
            throw new UserException(ErrorCode.ACCOUNT_EMAIL_DUPLICATION);
        }
    }
}
