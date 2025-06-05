package com.seolyu.userservice.domain.account.domain.repository;

import com.seolyu.userservice.domain.account.domain.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByEmail(String email);
    Optional<Account> findByEmail(String email);
}
