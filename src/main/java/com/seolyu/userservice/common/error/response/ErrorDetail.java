package com.seolyu.userservice.common.error.response;

import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ErrorDetail {
    private String message;
    private String field;

    public static ErrorDetail createByError(ObjectError error) {
        if (StringUtils.isBlank(error.getDefaultMessage()))
            return new ErrorDetail();

        return ErrorDetail.builder()
                .message(error.getDefaultMessage())
                .field(error instanceof FieldError ? ((FieldError) error).getField() : null)
                .build();
    }
}