package com.burger.stock_manager.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
        // 인터셉터 제거 - Spring Security가 인증/권한 처리
}