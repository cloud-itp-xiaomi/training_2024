package com.example.springcloud.base;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.List;

/**
 * @ClassName CfgConfig
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-22 23:19
 **/
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
