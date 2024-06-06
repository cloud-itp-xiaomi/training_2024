package com.example.demo.config;

import com.example.demo.interceptor.MyInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyWebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
// 注册拦截器，并配置拦截请求
        registry.addInterceptor( new MyInterceptor() )
                .addPathPatterns("/**")
                .excludePathPatterns("/","/login","/**/*.js","/**/*.css","/**/*.html","/images/**"
                        ,"*/sendSimpleEmail","*/sendAttachmentMail","*/sendImageMail","*/sendTemplateMail","*/activate/{id}");
    }
}