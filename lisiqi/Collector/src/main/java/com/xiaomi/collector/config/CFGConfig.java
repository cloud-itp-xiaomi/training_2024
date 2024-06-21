package com.xiaomi.collector.config;

import java.util.List;

public class CFGConfig {
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
