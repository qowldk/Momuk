// src/main/java/com/uplus/eureka/config/WebConfig.java
package com.uplus.eureka.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;

    public WebConfig(JwtInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/",
                        "/error",
                        "/favicon.ico",
                        "/api/users/login",
                        "/api/users/signup",
                        "/api/users/logout/{userId}",
                        "/api/votes",
                        "/api/votes/{voteId}",
                        "/api/votes/auto-close",
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/webjars/**"
                );
    }
}