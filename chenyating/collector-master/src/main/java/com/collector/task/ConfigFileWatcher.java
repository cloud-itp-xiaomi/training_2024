package com.collector.task;

import cn.hutool.json.JSONUtil;
import com.collector.bean.enums.SaveTypeEnum;
import com.collector.bean.response.LogResponse;
import com.collector.utils.Common;
import com.collector.utils.SaveLogByFile;
import com.collector.utils.SaveLogByMysql;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

@Component
@Slf4j
@EnableScheduling
public class ConfigFileWatcher {
    private final String aFilePath = "/home/work/a.log";
    private final String bFilePath = "/home/work/b.log";
    private final String cfgJsonFilePath = "/home/work/cfg.json";

    @Resource
    private SaveLogByMysql saveLogByMysql;
    @Resource
    private SaveLogByFile saveLogByFile;
    @Resource
    private Common common;

    private static LogResponse logResponse;

    // 每隔60s采集一次数据
    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void uploadLogTask(){
        log.info("收集日志开始!");

        if (null == logResponse) {
            synchronized (this){
                while (null == logResponse){
                    setLogResponse();
                }
            }
        }
        //存储类型
        String logStorage = logResponse.getLogStorage();
        //存储文件位置
        List<String> files = logResponse.getFiles();

        if (Objects.equals(logStorage, SaveTypeEnum.LOCAL_FILE.getMessage())) {
            saveLogByFile.saveLogs(files);
        } else {
            saveLogByMysql.saveLogs(files);
        }
    }

    @Bean
    public void configFileWatcherModify(){
        String filePath = cfgJsonFilePath;
        File file = new File(filePath);
        // 获取父目录
        File parentDir = file.getParentFile();
        if (parentDir != null) {
            System.out.println("[configFileWatcherModify]父目录: " + parentDir.getAbsolutePath());
        }

        if (!file.exists() || file.length() == 0) {
            log.error("文件不存在或为空");
            common.createFile(filePath);
            String contentToWrite = "{\n" +
                    "  \"files\": [\n" +
                    "    \"" + aFilePath + "\" ,\n" +
                    "    \"" + bFilePath + "\" \n" +
                    "  ],\n" +
                    "  \"log_storage\": \"" + SaveTypeEnum.LOCAL_FILE.getMessage() + "\"\n" +
                    "}";

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write(contentToWrite);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (null == logResponse){
                synchronized (this){
                    while (null == logResponse){
                        setLogResponse();
                    }
                }
            }
        }

        Common.executorService.schedule(() -> {
            Path path = Paths.get(cfgJsonFilePath);
            while (true){
                WatchService watcher;
                try {
                    watcher = FileSystems.getDefault().newWatchService();
                    path.getParent().register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);

                    // 初始化配置
                    String config = new String(Files.readAllBytes(path));
                    logResponse = JSONUtil.toBean(config, LogResponse.class);
                    // log.info("Current config: " + config);

                    WatchKey key = watcher.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        // 判断文件是否被修改
                        if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                            Path changedPath = (Path) event.context();
                            // 判断修改的文件是否是cfg.json
                            if (path.endsWith(changedPath)) {
                                String newConfig = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
                                // log.info("Config updated: " + newConfig);
                                // 处理新的配置
                                logResponse = JSONUtil.toBean(newConfig, LogResponse.class);
                                // 监听到文件修改,立即调用日志收集
                                // uploadLogTask();
                            }
                        }
                    }
                    boolean reset = key.reset();
                    if(!reset){
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(e.getMessage());
                }
            }
        }, 10, TimeUnit.SECONDS);
    }

    private void setLogResponse(){
        logResponse = new LogResponse();
        //则默认使用log_storage来存储日志文件
        logResponse.setFiles(Arrays.asList(aFilePath,bFilePath));
        logResponse.setLogStorage(SaveTypeEnum.LOCAL_FILE.getMessage());
    }
}
