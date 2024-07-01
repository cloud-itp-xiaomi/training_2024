package com.hw.collector.task;

import com.hw.collector.manager.LogMonitorManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author mrk
 * @create 2024-06-06-15:55
 */
@Component
@RequiredArgsConstructor
public class LogCollector {

    private final LogMonitorManager logMonitorManager;

    @PostConstruct
    public void startMonitor() {
        logMonitorManager.startMonitor();
    }

}
