package com.lx.collector.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.lx.collector.config.CfgConfig;
import com.lx.collector.pojo.LogMessage;
import com.lx.collector.service.LogService;
import com.lx.collector.utils.GetBeanUtil;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.InetAddress;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Data
@DependsOn(value = "getBeanUtil")
public class LogServiceImpl implements LogService {

    private CfgConfig config;//配置文件对应类

    private String hostName;

    private Map<String, Long> lastPositions;//被监听日志文件上一次读取的位置

    private RocketMQTemplate rocketMQTemplate = GetBeanUtil.getBean(RocketMQTemplate.class);

    private static String TOPIC_LOG = "HOST_LOG_TOPIC";//日志文件消息主题

    private static String TOPIC_CONFIG = "HOST_CONFIG_TOPIC";//配置文件消息主题

    private String CFG_JSON_PATH = "collector/src/main/java/com/lx/collector/log/cfg.json";//cfg.json文件路径

    private ExecutorService executorService;//线程池

    private String lastLogStorage;//上一次存储的方式

    public LogServiceImpl() {
        try {
            //解析cfg.json配置文件,赋值给config
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(CFG_JSON_PATH);
            config = objectMapper.readValue(file, CfgConfig.class);
            //获取主机名
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> files = config.getFiles(); //被监听的日志文件
        executorService = Executors.newFixedThreadPool(files.size() + 1);
        lastPositions = new HashMap<>();
        for (String file : files) {
            lastPositions.put(file, 0L);
        }
        lastLogStorage = config.getLog_storage();//初始化存储方式
    }

    //执行完构造方法后开始监听日志文件
    @PostConstruct
    public void startMonitor() {
        List<String> files = config.getFiles();
        Path pathA = Paths.get(files.get(0));
        Path pathB = Paths.get(files.get(1));

        // 创建任务
        LogTask logTaskA = new LogTask("taskA", pathA);
        LogTask logTaskB = new LogTask("taskB", pathB);
        ConfigTask configTask = new ConfigTask();

        //提交任务
        executorService.submit(logTaskA);
        executorService.submit(logTaskB);
        executorService.submit(configTask);

        //关闭线性池
        executorService.shutdown();
    }

    /**
     * 读取日志文件的新增内容,并发送给RocketMQ
     */
    @Override
    public void readAndReport(Path filePath) {
        try (RandomAccessFile file = new RandomAccessFile(filePath.toFile(), "r")) {
            Long lastPosition = lastPositions.get(filePath.toString());
            Long fileSize = Files.size(filePath);
            //日志文件新增了内容,进行读取操作
            if (lastPosition < fileSize) {
                file.seek(lastPosition);
                List<String> newLogs = new ArrayList<>();//新增日志
                String line;
                while ((line = file.readLine()) != null) {
                    //不读取空行
                    if (!line.isEmpty()) {
                        newLogs.add(line);
                    }
                }
                lastPositions.put(filePath.toString(), file.getChannel().position());
                if (newLogs.size() > 0) {
                    LogMessage logMessage = new LogMessage();
                    logMessage.setHostName(hostName);
                    logMessage.setFile(filePath.toString());
                    logMessage.setLogs(newLogs);
                    System.out.println("send a message to " + LogServiceImpl.TOPIC_LOG);
                    System.out.println(logMessage);
                    Gson gson = new Gson();
                    String logGson = gson.toJson(logMessage);
                    rocketMQTemplate.convertAndSend(LogServiceImpl.TOPIC_LOG, logGson);//发送消息至消息队列"HOST_LOG_TOPIC"主题
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
                System.out.println("send a message to " + TOPIC_CONFIG + " , this message is \"" + logStorage + "\"");
                rocketMQTemplate.convertAndSend(TOPIC_CONFIG, logStorage);
                lastLogStorage = logStorage;//重置lastLogStorage
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 使用 WatchService 监听日志文件变化
     class LogTask implements Runnable {

        private String name;
        private Path path;

        public LogTask(String name, Path path) {
            this.name = name;
            this.path = path;
        }

        @Override
        public void run() {
            try {
                WatchService watchService = FileSystems.getDefault().newWatchService();
                path.getParent().register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);//将所有要监听的日志文件目录注册到watchService
                //监听日志文件变化
                while (true) {
                    WatchKey key = null;
                    key = watchService.take();
                    //处理监听到的事件
                    for (WatchEvent<?> event : key.pollEvents()) {
                        WatchEvent.Kind<?> kind = event.kind();//获取事件类型
                        //日志文件中新增内容
                        if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                            WatchEvent<Path> watchEvent = (WatchEvent<Path>) event;
                            Path context = watchEvent.context();//获取相对路径
                            String relativePath = context.toString().substring(0, context.toString().length() - 1);
                            if (relativePath.equals(path.getFileName().toString())) {
                                readAndReport(path.resolve(path.toString()));//读取文件新增内容并上报
                            }
                        }
                    }
                    boolean valid = key.reset();//重置watchKey
                    if (!valid) {
                        break;
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
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
