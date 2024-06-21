package com.cl.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * 日志存储方式config
 *
 * @author: tressures
 * @date: 2024/6/12
 */
@Configuration
@RefreshScope
public class LogStorageConfig {

    @Value("${log_storage}")
    private String log_storage;

    @RefreshScope
    public String getLogStorage(){
        return this.log_storage;
    }
}
