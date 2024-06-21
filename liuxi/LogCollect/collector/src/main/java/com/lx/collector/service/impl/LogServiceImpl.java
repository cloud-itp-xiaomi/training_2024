package com.lx.collector.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.lx.collector.config.CfgConfig;
import com.lx.collector.pojo.LogFileListener;
import com.lx.collector.pojo.LogMessage;
import com.lx.collector.service.LogService;
import com.lx.collector.utils.GetBeanUtil;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Data
@DependsOn(value = "getBeanUtil")
public class LogServiceImpl implements LogService {

    private CfgConfig config;//配置文件对应类

    private RocketMQTemplate rocketMQTemplate = GetBeanUtil.getBean(RocketMQTemplate.class);

    private static String TOPIC_CONFIG = "HOST_CONFIG_TOPIC";//配置文件消息主题

    private String CFG_JSON_PATH = "collector/src/main/java/com/lx/collector/log/cfg.json";//cfg.json文件路径

    private ExecutorService executorService;

    private String lastLogStorage;//上一次存储的方式

    public LogServiceImpl() {
        try {
            //解析cfg.json配置文件,赋值给config
            ObjectMapper objectMapper = new ObjectMapper();
            File configFile = new File(CFG_JSON_PATH);
            config = objectMapper.readValue(configFile, CfgConfig.class);
            executorService = Executors.newSingleThreadExecutor();
            lastLogStorage = config.getLog_storage();//初始化存储方式
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //执行完构造方法后开始监听日志文件
    @PostConstruct
    public void startMonitor() {
        // 监听日志文件
        for (String filePath : config.getFiles()) {
            File file = new File(filePath);
            FileAlterationObserver observer = new FileAlterationObserver(file.getParentFile());
            observer.addListener(new LogFileListener(file));
            // 创建文件监控器并启动
            FileAlterationMonitor monitor = new FileAlterationMonitor(1000, observer);
            try {
                monitor.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //监听配置文件
        executorService.execute(new ConfigTask());
    }

    //读取配置文件
    @Override
    public void readConfigAndReport() {
        //读取配置文件
        File file = new File(CFG_JSON_PATH);
        try (RandomAccessFile randomFile = new RandomAccessFile(file, "r")) {
            randomFile.seek(0L);
            String line;
            String logStorage = "";
            while ((line = randomFile.readLine()) != null) {
                if (line.contains("\"log_storage\"")) {
                    // 提取log_storage的值
                    int startIndex = line.indexOf(":") + 3;
                    int endIndex = line.lastIndexOf("\"");
                    logStorage = line.substring(startIndex, endIndex);
                    System.out.println("log_storage: " + logStorage);
                    break;
                }
            }
            if (!lastLogStorage.equals(logStorage) && !"".equals(logStorage)) {
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonMessage = objectMapper.writeValueAsString(logStorage);
                System.out.println("send a message to " + TOPIC_CONFIG + " , this message is \"" + logStorage + "\"");
                rocketMQTemplate.convertAndSend(TOPIC_CONFIG, jsonMessage);
                lastLogStorage = logStorage;//重置lastLogStorage
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //监听配置文件中存储方式是否发生变化
    class ConfigTask implements Runnable {

        @Override
        public void run() {
            try {
                WatchService watchService = FileSystems.getDefault().newWatchService();
                Path configFilePath = Paths.get(CFG_JSON_PATH);
                configFilePath.getParent().register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);//注册配置文件到watchService
                while (true) {
                    WatchKey key = watchService.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                            WatchEvent<Path> watchEvent = (WatchEvent<Path>) event;
                            Path context = watchEvent.context();//获取相对路径
                            String relativePath = context.toString().substring(0, context.toString().length() - 1);
                            if (relativePath.equals(configFilePath.getFileName().toString())) {
                                readConfigAndReport();//读取配置方式并上报
                            }
                        }
                    }
                    boolean valid = key.reset();
                    if (!valid) {
                        break;
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
