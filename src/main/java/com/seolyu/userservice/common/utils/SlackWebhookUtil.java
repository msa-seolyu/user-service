package com.seolyu.userservice.common.utils;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SlackWebhookUtil {
    private final Slack slackClient = Slack.getInstance();

    @Value("${slack.webhook.error-alarm}")
    private String SLACK_WEBHOOK_ERROR_ALARM_URL;

    /**
     * 슬랙 에러 메시지 전송
     **/
    public void sendErrorMessage(String title, String content) throws IOException {
        String message = "*" + title + "*\n```\n" + content + "\n```";
        Payload payload = Payload.builder()
                .text(message)
                .build();

        slackClient.send(SLACK_WEBHOOK_ERROR_ALARM_URL, payload);
    }
}
