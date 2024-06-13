package org.example.rest;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Data
@Configuration
@ConfigurationProperties(prefix = "param")
public class RestConfig {
    private String utilizationUrl;
    private String logUrl;
    private String cpuPath;
    private String memoryPath;
}
