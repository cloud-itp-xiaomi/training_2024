package com.jiuth.sysmonitorcapture.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.List;

@Configuration
public class SysLogConfig {


    public static class Config {
        public List<String> files;
        public String log_storage;
    }

    @Bean
    public Config logConfig() throws IOException {
        ObjectMapper objectMapper= new ObjectMapper();
        return objectMapper.readValue(
                getClass().getClassLoader().getResource("cfg.json"),
                Config.class
        );
    }

    public List<String> getFiles() throws IOException {
        return logConfig().files;
    }

}