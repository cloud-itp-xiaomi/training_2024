package com.hw.collector.manager;

import com.hw.collector.listener.LogFileListener;
import com.hw.collector.utils.CollectUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

/**
 * @author mrk
 * @create 2024-06-10-23:23
 */
@Component
@RequiredArgsConstructor
public class LogMonitorManager {

    private final CollectUtil collectUtil;
    private final LogFileListener logFileListener;
//    private final LogConfig logConfig;
    private FileAlterationMonitor monitor;

    @PostConstruct
    public void initMonitor() throws IOException {
        String[] logFilePaths = collectUtil.getLogFilePaths("/cfg.json");
//        String[] logFilePaths = logConfig.getFiles();
        monitor = new FileAlterationMonitor(10000);
        for (String filePath : logFilePaths) {
            File file = new File(filePath);
            // observer 观察的对象是某个目录，因此要通过 getParentFile() 获得文件所在父目录
            FileAlterationObserver observer = new FileAlterationObserver(file.getParentFile());
            observer.addListener(logFileListener);
            monitor.addObserver(observer);
        }
    }

    public void startMonitor() {
        if (monitor != null) {
            try {
                monitor.start();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
