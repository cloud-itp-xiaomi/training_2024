package com.example.mi1.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "jdbc")
public class JdbcProperties {
    private String driver;
    private String url;
    private String username;
    private String password;
}
