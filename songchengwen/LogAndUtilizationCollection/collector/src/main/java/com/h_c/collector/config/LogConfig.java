package com.h_c.collector.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.util.List;

@Configuration
public class LogConfig {
    @Value("${config.file.path}")
    private String configFilepath;

    @Bean
    public LogCollectorConfig logCollectorConfig() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new FileSystemResource(configFilepath).getInputStream(), LogCollectorConfig.class);
    }

    public static class LogCollectorConfig {
        @JsonProperty("files")
        public List<String> files;
        @JsonProperty("log_storage")
        public String logStorage;
    }


}
