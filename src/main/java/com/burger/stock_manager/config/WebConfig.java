package com.burger.stock_manager.config;

import com.burger.stock_manager.interceptor.RoleCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private RoleCheckInterceptor roleCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(roleCheckInterceptor)
                // 조회(/inventory)는 빼고, 데이터를 변경하는 핵심 기능들만 묶어서 감시
                .addPathPatterns("/add-stock", "/delete-stock", "/update-stock")
                .excludePathPatterns("/login", "/register", "/css/**", "/js/**");
    }
}