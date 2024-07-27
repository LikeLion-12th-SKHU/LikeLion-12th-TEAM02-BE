package com.skhu.moodfriend.global.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class ImpConfig {

    @Value("${imp.code}")
    private String code;

    @Value("${imp.api.key}")
    private String apiKey;

    @Value("${imp.api.secret-key}")
    private String apiSecretKey;
}
