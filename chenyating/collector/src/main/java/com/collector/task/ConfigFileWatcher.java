package com.collector.task;

import cn.hutool.json.JSONUtil;
import com.collector.bean.enums.SaveTypeEnum;
import com.collector.bean.response.SaveResponse;
import com.collector.utils.Common;
import com.collector.utils.SaveLogByFile;
import com.collector.utils.SaveLogByMysql;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
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
    private static SaveResponse saveResponse;

    // 每隔60s采集一次数据
    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void uploadLogTask() throws IOException {
        log.info("收集日志开始!");
        setLogResponse();

        // 用Path处理路径，兼容性，安全，方便。
        Path path = Paths.get(cfgJsonFilePath);
        String config = new String(Files.readAllBytes(path));
        // 检查是否不为空白(不为null、不为空格符和长度不为0)
        if(StringUtils.isNotBlank(config)){
            String logStorage = saveResponse.getLogStorage();
            List<String> files = saveResponse.getFiles();

            if (Objects.equals(logStorage, SaveTypeEnum.LOCAL_FILE.getMessage())) {
                saveLogByFile.saveLogs(files);
            } else {
                saveLogByMysql.saveLogs(files);
            }
        }
    }

    public void setLogResponse() {
        if (null == saveResponse) {
            synchronized (this) {
                while (null == saveResponse) {
                    saveResponse = new SaveResponse();
                    //则默认使用log_storage来存储日志文件
                    saveResponse.setFiles(Arrays.asList(aFilePath, bFilePath));
                    saveResponse.setLogStorage(SaveTypeEnum.LOCAL_FILE.getMessage());
                }
            }
        }
    }

    @Bean
    public void configFileWatcherModify() {
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
            setLogResponse();
        }

        // 文件监控
        Common.executorService.schedule(() -> {
            Path path = Paths.get(cfgJsonFilePath);
            while (true) {
                WatchService watcher;
                try {
                    watcher = FileSystems.getDefault().newWatchService();
                    path.getParent().register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);

                    String config = new String(Files.readAllBytes(path));
                    if (StringUtils.isNotBlank(config)) {
                        saveResponse = JSONUtil.toBean(config, SaveResponse.class);

                        WatchKey key = watcher.take();
                        for (WatchEvent<?> event : key.pollEvents()) {
                            // 判断文件是否被修改
                            if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                                Path changedPath = (Path) event.context();
                                // 判断修改的文件是否是cfg.json，处理新的配置
                                if (path.endsWith(changedPath)) {
                                    String newConfig = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
                                    saveResponse = JSONUtil.toBean(newConfig, SaveResponse.class);
                                }
                            }
                        }
                        boolean reset = key.reset();
                        if (!reset) {
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(e.getMessage());
                }
            }
        }, 10, TimeUnit.SECONDS);
    }
}
