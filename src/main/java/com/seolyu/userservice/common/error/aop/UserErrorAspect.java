package com.seolyu.userservice.common.error.aop;

import com.seolyu.userservice.common.utils.SlackWebhookUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class UserErrorAspect {
    private final SlackWebhookUtil slackWebhookUtil;

    /**
     * exception handler pointcut
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.ExceptionHandler)")
    public void exceptionHandler() {
    }

    /**
     * record error log
     * exceptionHandler return 후 error log를 찍어준다.
     * error log format : ERR > httpStatus=${httpStatus}, errorCode="${errorCode}", errorType="${errorType}", message="${message}",\nstackTrace="${stackTrace}"
     *
     * @param joinPoint   Exception
     * @param returnValue error response (error DTO)
     */
    @AfterReturning(pointcut = "exceptionHandler()", returning = "returnValue")
    public void sendSlackErrorAlam(JoinPoint joinPoint, Object returnValue) {
        Exception exception = (Exception) joinPoint.getArgs()[0];

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(returnValue);
        } catch (JSONException e) {
            return;
        }

        if (jsonObject.isEmpty()) {
            return;
        }

        Integer httpStatus = null;
        if (jsonObject.has("httpStatus")) {
            httpStatus = jsonObject.getInt("httpStatus");
        } else if (jsonObject.has("status")) {
            httpStatus = jsonObject.getInt("status");
        } else if (jsonObject.has("statusCodeValue")) {
            httpStatus = jsonObject.getInt("statusCodeValue");
        }


        if (httpStatus == null || !HttpStatus.valueOf(httpStatus).is5xxServerError()) {
            return;
        }

        String errorCode = "";
        if (jsonObject.has("errorCode")) {
            errorCode = jsonObject.getString("errorCode");
        } else if (jsonObject.has("code")) {
            errorCode = jsonObject.getString("code");
        } else if (jsonObject.has("properties")) {
            JSONObject properties = jsonObject.getJSONObject("properties");
            if (properties.has("errorCode")) {
                errorCode = properties.getString("errorCode");
            }
        } else if (jsonObject.has("body")) {
            JSONObject body = jsonObject.getJSONObject("body");
            if (body.has("code")) {
                errorCode = body.getString("code");
            } else if (body.has("errorCode")) {
                errorCode = body.getString("errorCode");
            }
        }

        String message = "";
        if (jsonObject.has("message")) {
            message = jsonObject.getString("message");
        } else if (jsonObject.has("detail")) {
            message = jsonObject.getString("detail");
        } else if (jsonObject.has("body")) {
            JSONObject body = jsonObject.getJSONObject("body");
            if (body.has("message")) {
                message = body.getString("message");
            } else if (body.has("errorMessage")) {
                message = body.getString("errorMessage");
            }
        }

        String errorType = exception.getClass().getName();
        StringBuilder stackTrace = new StringBuilder();
        if (errorType.equals("org.springframework.web.bind.MethodArgumentNotValidException")) {
            MethodArgumentNotValidException e = (MethodArgumentNotValidException) exception;
            stackTrace.append(String.format("[%s] %s", Objects.requireNonNull(e.getBindingResult().getFieldError()).getField(),
                    e.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
        } else {
            stackTrace.append(exception.getMessage());
        }

        // stackTrace 출력
        List<String> list = Arrays.stream(exception.getStackTrace()).map(Object::toString).toList();
        for (String l : list) {
            stackTrace.append("\n").append(l);
        }

        String errorMessage = String.format("ERR > httpStatus=%d, errorCode=\"%s\", errorType=\"%s\", message=\"%s\",\nstackTrace=\"%s\"",
                httpStatus, errorCode, errorType, message, stackTrace);

        // 500번대 에러인 경우 슬랙 알람 전송
        String requestId = MDC.get("requestId");
        String title = "[user-service] 500번대(서버) 오류가 났어요! [requestId] " + requestId;
        try {
            slackWebhookUtil.sendErrorMessage(title, errorMessage);
        } catch (IOException e) {
            log.error("send slack message failed!", e);
        }
    }
}
