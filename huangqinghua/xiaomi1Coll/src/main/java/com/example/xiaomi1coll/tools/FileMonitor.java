package com.example.xiaomi1coll.tools;

import com.example.xiaomi1coll.entity.FileConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.monitor.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@Configuration
public class FileMonitor {

    private static final Logger logger = LoggerFactory.getLogger(FileMonitor.class);
    private static final long POLL_INTERVAL = 5000; // 5 seconds

    @Value("classpath:cfg.json")
    private Resource configFile;

    @Value("${server.hostname}")
    private String hostname;

    /**
     * 初始化文件监控器，在Spring容器初始化完成后执行
     */
    @PostConstruct
    public void startMonitoring() {
        try {
            FileConfig fileConfig = loadConfig();

            for (String filePath : fileConfig.getFiles()) {
                File file = new File(filePath);
                FileAlterationObserver observer = new FileAlterationObserver(file.getParentFile());
                observer.addListener(new LogFileListener(file, hostname, fileConfig.getLog_storage()));

                // 创建文件监控器并启动
                FileAlterationMonitor monitor = new FileAlterationMonitor(POLL_INTERVAL, observer);
                monitor.start();
                logger.info("Started monitoring files: {}", fileConfig.getFiles());
            }
        } catch (Exception e) {
            logger.error("Error starting file monitor", e);
        }
    }

    /**
     * 加载配置文件
     * @return FileConfig 配置文件对象
     * @throws IOException 如果文件读取失败
     */
    private FileConfig loadConfig() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(configFile.getInputStream(), FileConfig.class);
    }
}
