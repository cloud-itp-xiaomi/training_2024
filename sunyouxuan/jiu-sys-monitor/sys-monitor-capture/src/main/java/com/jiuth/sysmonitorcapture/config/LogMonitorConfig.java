package com.jiuth.sysmonitorcapture.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


/**
 * @author jiuth
 */
@Configuration
public class LogMonitorConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public Config logMonitorConfiguration(ObjectMapper objectMapper) throws IOException {
//        return objectMapper.readValue(
//                getClass().getClassLoader().getResource("cfg.json"),
//                Config.class
//        );

        Config config = objectMapper.readValue(
                getClass().getClassLoader().getResource("cfg.json"),
                Config.class
        );

        for (int i = 0; i < config.getFiles().size(); i++) {
            String filePath = config.getFiles().get(i);
            Path path = Paths.get(filePath);
            if (!path.isAbsolute()) {
                filePath = Paths.get(System.getProperty("user.dir"), filePath).normalize().toString();
                config.getFiles().set(i, filePath);
            } else {
                filePath = path.normalize().toString();
                config.getFiles().set(i, filePath);
            }
        }
        return config;
    }

    public static class Config {
        @JsonProperty("files")
        private List<String> files;

        @JsonProperty("log_storage")
        private String logStorage;

        // Getters and Setters
        public List<String> getFiles() {
            return files;
        }

        public void setFiles(List<String> files) {
            this.files = files;
        }

        public String getLogStorage() {
            return logStorage;
        }

        public void setLogStorage(String logStorage) {
            this.logStorage = logStorage;
        }
    }
}
