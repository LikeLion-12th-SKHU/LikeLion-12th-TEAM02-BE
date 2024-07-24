package com.skhu.moodfriend.global.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.skhu.moodfriend.app.repository")
public class FeignConfig {
}
