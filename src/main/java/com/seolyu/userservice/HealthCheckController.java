package com.seolyu.userservice;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "health-check")
@RestController
public class HealthCheckController {
    @GetMapping("/health-check")
    @ResponseStatus(HttpStatus.OK)
    public String check() {
        return "success!";
    }
}
