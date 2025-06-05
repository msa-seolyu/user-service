package com.seolyu.userservice.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 공통
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "10000", "잘못된 요청입니다."),
    INVALID_FORMAT(HttpStatus.BAD_REQUEST, "10001", "잘못된 입력 데이터입니다."),
    URL_NOT_FOUND(HttpStatus.NOT_FOUND, "10002", "URL not found"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "10003", "서버에서 처리 중 에러가 발생하였습니다."),
    INVALID_JSON_FORMAT(HttpStatus.BAD_REQUEST, "10004", "JSON 형식이 잘못되었습니다."),
    FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "10005", "파일 업로드 실패하였습니다."),
    INVALID_AUTHORITY(HttpStatus.FORBIDDEN, "10006", "접근 권한이 없습니다."),
    FILE_DOWNLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "10007", "File Download Error"),

    // 인증, 인가
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "10500", "토큰이 만료되었습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "10501", "토큰이 올바르지 않습니다."),
    INVALID_LOGIN_EMAIL(HttpStatus.BAD_REQUEST, "10502", "이메일을 확인해 주세요."),
    INVALID_LOGIN_PASSWORD(HttpStatus.BAD_REQUEST, "10503", "비밀번호는 영어 대소문자 및 숫자를 포함하여 8자 이상 입력해 주세요."),
    INVALID_ACCOUNT(HttpStatus.UNAUTHORIZED, "10504", "이메일 또는 비밀번호를 확인해 주세요."),
    MISSING_TOKEN(HttpStatus.UNAUTHORIZED, "10505", "토큰이 없습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "10506", "지원되지 않는 HTTP 메서드입니다. POST 요청만 허용됩니다."),

    // Account
    ACCOUNT_EMAIL_DUPLICATION(HttpStatus.BAD_REQUEST, "20000", "이미 가입된 이메일이에요."),
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "20001", "해당 계정을 찾을 수 없습니다.")

    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
