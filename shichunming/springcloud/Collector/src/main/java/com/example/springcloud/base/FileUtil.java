package com.example.springcloud.base;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName FileUtil
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-23 22:26
 **/
@Component
public class FileUtil {
    @Value("${directory.watch}")
    private String path;
    @Value("${directory.interval}")
    private long interval;
    public void execute() throws Exception {
        FileMonitor fileMonitor = new FileMonitor(interval);
        fileMonitor.monitor(path, new FileListener());
        fileMonitor.start();
    }

    public FileUtil() {
    }
}
