package com.seolyu.userservice.common.error.response;

import com.seolyu.userservice.common.error.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ErrorsResponse {
    private final int status;
    private final String code;
    private final String message;
    private final List<ErrorDetail> details;

    public static ErrorsResponse create(ErrorCode errorCode, Errors errors) {
        List<ErrorDetail> errorDetails = errors.getFieldErrors()
                .stream()
                .map(ErrorDetail::createByError)
                .collect(Collectors.toList());

        errorDetails.addAll(errors.getGlobalErrors()
                .stream()
                .map(ErrorDetail::createByError)
                .toList());

        return ErrorsResponse.builder()
                .status(errorCode.getStatus().value())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .details(errorDetails)
                .build();
    }

    public static ErrorsResponse create(ErrorCode errorCode) {
        return ErrorsResponse.builder()
                .status(errorCode.getStatus().value())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .details(new ArrayList<>())
                .build();
    }

    public static ErrorsResponse create(int status, String code, String message) {
        return ErrorsResponse.builder()
                .status(status)
                .code(code)
                .message(message)
                .details(new ArrayList<>())
                .build();
    }
}
