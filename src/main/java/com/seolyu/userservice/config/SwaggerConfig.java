package com.seolyu.userservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@RequiredArgsConstructor
@Configuration
@ConditionalOnExpression(value = "${springdoc.swagger-ui.enabled:false}")
public class SwaggerConfig {
    private final String AUTHORIZE = "JWT Bearer";

    @Value("${springdoc.swagger-ui.docs-url}")
    private String url;

    @Bean
    public OpenAPI customOpenAPI(@Value("${spring.profiles.active}") String active,
                                 @Value("${server.port}") String port) {
        return new OpenAPI()
                .info(info())
                .addServersItem(server(active, port))
                .addSecurityItem(securityRequirement())
                .schemaRequirement(this.AUTHORIZE, jwtSecurityScheme())
                ;
    }

    // 환경에 따라서 URL 제공
    private Server server(String active, String port) {
        if (StringUtils.equals(active, "local")) {
            return new Server().url("http://localhost:" + port);
        }

        return new Server().url(url);
    }

    // API INFO
    private Info info() {
        return new Info()
                .title("Seolyu User Service Backend")
                .description("User Service API")
                .version("v1");
    }

    // 적용할 security 정보
    private SecurityScheme jwtSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .description("jwt token accessKey");
    }

    // security 추가
    private SecurityRequirement securityRequirement() {
        return new SecurityRequirement().addList(this.AUTHORIZE);
    }
}