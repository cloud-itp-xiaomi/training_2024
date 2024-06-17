package com.jiuth.sysmonitorcapture;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileMonitorApp {

    public static void main(String[] args) {
        // 创建 ObjectMapper 实例
        ObjectMapper objectMapper = new ObjectMapper();

        // 创建 FileMonitorService 实例并传入 ObjectMapper
        FileMonitorService fileMonitorService = new FileMonitorService(objectMapper);

        // 要监控的文件列表
        List<String> filesToMonitor = List.of(
                "/home/jiuth/1-Code/2024_training/1-log/memoryCollector.log",
                "/home/jiuth/1-Code/2024_training/1-log/cpuCollector.log"
        );

        String file =filesToMonitor.get(0);

        try {
                    fileMonitorService.monitorFile(file);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
        // 使用线程池监听文件变化
//        ExecutorService executorService = Executors.newFixedThreadPool(2);
//        for (String file : filesToMonitor) {
//            executorService.submit(() -> {
//                try {
//                    fileMonitorService.monitorFile(file);
//                } catch (IOException | InterruptedException e) {
//                    e.printStackTrace();
//                }
//            });
//        }
    }
}