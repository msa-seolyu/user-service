package com.seolyu.userservice.common.error.exception;

import com.seolyu.userservice.common.error.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SeolyuException extends RuntimeException {
    private final ErrorCode errorCode;

    public SeolyuException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public SeolyuException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getCode() {
        return this.errorCode.getCode();
    }

    public String getErrorMessage() {
        return this.getMessage();
    }

    public HttpStatus getStatus() {
        return this.errorCode.getStatus();
    }
}
