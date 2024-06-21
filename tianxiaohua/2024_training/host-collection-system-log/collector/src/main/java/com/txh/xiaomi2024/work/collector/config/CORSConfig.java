package com.txh.xiaomi2024.work.collector.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CORSConfig implements WebMvcConfigurer {
    static final String[] ORIGINS = new String[]{"GET", "POST", "PUT", "DELETE", "OPTIONS"};

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders(ORIGINS)
                .allowedMethods(ORIGINS)
                .allowCredentials(true)
                .allowedMethods(CorsConfiguration.ALL)
                .maxAge(3600); // 1小时内不需要再预检（发OPTIONS请求）
    }

}