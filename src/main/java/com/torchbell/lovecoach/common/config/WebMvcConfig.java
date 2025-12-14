package com.torchbell.lovecoach.common.config;

import com.torchbell.lovecoach.common.interceptor.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**") // 모든 요청에 대해 적용
                .excludePathPatterns(
                        "/api/users/join", // 회원가입
                        "/api/users/login", // 로그인
                        "/api/users/logout", // 로그아웃
                        "/swagger-ui/**", // Swagger UI
                        "/v3/api-docs/**", // Swagger API Docs
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/error" // 에러 페이지
                );
    }
}
