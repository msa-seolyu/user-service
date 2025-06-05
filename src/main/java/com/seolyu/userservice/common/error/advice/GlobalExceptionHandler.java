package com.seolyu.userservice.common.error.advice;

import com.seolyu.userservice.common.error.ErrorCode;
import com.seolyu.userservice.common.error.exception.SeolyuException;
import com.seolyu.userservice.common.error.response.ErrorsResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SeolyuException.class)
    public ResponseEntity<ErrorsResponse> handleSeolyuException(SeolyuException e) {
        log.debug(e.toString(), e);
        return ResponseEntity.status(e.getStatus())
                .body(ErrorsResponse.create(e.getStatus().value(), e.getCode(), e.getErrorMessage()));
    }

    @ExceptionHandler(ClientAbortException.class)
    @ResponseStatus(HttpStatus.RESET_CONTENT)
    public void handleClientAbortException(ClientAbortException e) {
        log.debug("ClientAbortException is occurred. {}", e.toString());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsResponse handleValidationMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.debug(e.toString(), e);
        Errors errors = e.getBindingResult();
        return ErrorsResponse.create(ErrorCode.INVALID_FORMAT, errors);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsResponse handleBindException(BindException e) {
        log.debug(e.toString(), e);
        Errors errors = e.getBindingResult();
        return ErrorsResponse.create(ErrorCode.INVALID_FORMAT, errors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.debug(e.toString(), e);
        return ErrorsResponse.create(ErrorCode.INVALID_FORMAT);
    }

    @ExceptionHandler(MissingRequestValueException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsResponse handleMissingRequestValueException(MissingRequestValueException e) {
        log.debug(e.toString(), e);
        return ErrorsResponse.create(ErrorCode.INVALID_FORMAT);
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorsResponse handleIOException(IOException e) {
        log.error(e.toString(), e);
        return ErrorsResponse.create(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorsResponse handleUnhandledException(Exception e) {
        log.error(e.toString(), e);
        return ErrorsResponse.create(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorsResponse handleNoHandlerFoundException(Exception e) {
        log.error(e.toString(), e);
        return ErrorsResponse.create(ErrorCode.URL_NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorsResponse handleHttpMessageNotReadableException(Exception e) {
        log.debug(e.toString(), e);
        return ErrorsResponse.create(ErrorCode.INVALID_JSON_FORMAT);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ErrorsResponse handleAccessDeniedException(AccessDeniedException e) {
        log.debug(e.toString(), e);
        return ErrorsResponse.create(ErrorCode.INVALID_AUTHORITY);
    }
}
