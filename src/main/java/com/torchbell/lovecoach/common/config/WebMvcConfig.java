package com.torchbell.lovecoach.common.config;

import com.torchbell.lovecoach.common.interceptor.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

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
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("") // [변경] 모든 IP에서의 요청 허용 (개발 단계에서 편리함)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowCredentials(true); // 쿠키/세션 인증을 위해 필요할 수 있음
    }
}
