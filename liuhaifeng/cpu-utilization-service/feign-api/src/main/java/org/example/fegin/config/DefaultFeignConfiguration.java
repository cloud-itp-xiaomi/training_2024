package org.example.fegin.config;


import feign.Logger;
import org.springframework.context.annotation.Bean;


/**
 * Feign日志配置
 *
 * @author liuhaifeng
 * @date 2024/05/30/21:14
 */
public class DefaultFeignConfiguration {

    @Bean
    public Logger.Level logLevel() {
        return Logger.Level.BASIC;
    }
}
