package com.h_c.collector.service;

import com.h_c.collector.config.LogConfig;
import com.h_c.collector.utils.FileWatcher;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogCollectorService {
    @Autowired
    private final LogConfig.LogCollectorConfig logCollectorConfig;

    public LogCollectorService(LogConfig.LogCollectorConfig logCollectorConfig) {
        this.logCollectorConfig = logCollectorConfig;
    }

    @PostConstruct
    private void startWatching() {
        FileWatcher fileWatcher = new FileWatcher(logCollectorConfig.files, logCollectorConfig.logStorage);
        fileWatcher.start();

    }
}
