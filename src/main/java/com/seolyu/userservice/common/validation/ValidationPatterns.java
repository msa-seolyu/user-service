package com.seolyu.userservice.common.validation;

public class ValidationPatterns {
    private ValidationPatterns() {}

    // 이메일 정규식: 최대 30자, 올바른 이메일 형식
    public static final String EMAIL = "(?i)^(?=.{1,30}$)[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

    // 비밀번호 정규식: 영문과 숫자 조합, 8자 이상 20자 이하
    public static final String PASSWORD = "(?i)^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,20}$";
}
