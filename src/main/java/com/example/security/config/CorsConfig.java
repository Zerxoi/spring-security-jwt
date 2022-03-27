package com.example.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                // 允许所有路径
                .addMapping("/**")
                // 允许所有域
                .allowedOriginPatterns("*")
                // 允许Cookie
                .allowCredentials(true)
                // 允许所有请求方式
                .allowedMethods("*")
                // 允许所有请求头
                .allowedHeaders("*")
                // 缓存时间
                .maxAge(3600);
    }
}
