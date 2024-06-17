package com.jiuth.sysmonitorcapture;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@MapperScan("com.jiuth.sysmonitorcapture.mapper")
@SpringBootApplication
@EnableScheduling
public class SysMonitorCaptureApplication {

    public static void main(String[] args) {
        // 输出系统属性，检查是否正确设置
        SpringApplication.run(SysMonitorCaptureApplication.class, args);

    }

}
