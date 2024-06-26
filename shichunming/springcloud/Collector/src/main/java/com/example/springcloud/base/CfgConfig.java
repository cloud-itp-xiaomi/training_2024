package com.example.springcloud.base;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app")
public class CfgConfig {
    private List<String> files;
    private String logStorage;
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