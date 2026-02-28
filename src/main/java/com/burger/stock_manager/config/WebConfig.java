package com.burger.stock_manager.config;

import com.burger.stock_manager.interceptor.LoginCheckInterceptor;
import com.burger.stock_manager.interceptor.AdminCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginCheckInterceptor loginCheckInterceptor;

    @Autowired
    private AdminCheckInterceptor adminCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(loginCheckInterceptor)
                .addPathPatterns("/inventory/**", "/sales-dashboard", "/sell-menu", "/add-stock", "/delete-stock",
                        "/update-stock")
                .excludePathPatterns("/login", "/register", "/css/**", "/js/**");

        registry.addInterceptor(adminCheckInterceptor)
                .addPathPatterns("/add-stock", "/delete-stock", "/update-stock");
    }
}