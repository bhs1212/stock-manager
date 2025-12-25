package com.burger.stock_manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // 비밀번호 암호화 도구(BCrypt)를 빈으로 등록
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 보안 설정상 CSRF는 일단 끕니다
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // 우리가 만든 세션 로그인을 쓸 것이므로 모두 허용
                )
                .formLogin(login -> login.disable()) // 스프링 기본 로그인창 안 쓰기
                .logout(logout -> logout.disable()); // 스프링 기본 로그아웃 안 쓰기

        return http.build();
    }
}
