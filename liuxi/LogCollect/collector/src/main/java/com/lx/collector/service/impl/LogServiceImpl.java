package com.lx.collector.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.lx.collector.config.CfgConfig;
import com.lx.collector.pojo.Log;
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

    private String hostName ;

    private Map<String, Long> lastPositions;//被监听日志文件上一次读取的位置

    private RocketMQTemplate rocketMQTemplate = GetBeanUtil.getBean(RocketMQTemplate.class);

    private static String TOPIC = "HOST_LOG_TOPIC";//消息主题

    private ExecutorService executorService;

    public LogServiceImpl() {
        try {
            String CFG_JSON_PATH = "E:\\java_study\\javaProjects\\LogCollect\\collector\\src\\main\\resources\\cfg.json";//cfg.json文件路径
            //解析cfg.json配置文件,赋值给config
            ObjectMapper objectMapper = new ObjectMapper();
            config = objectMapper.readValue(new File(CFG_JSON_PATH), CfgConfig.class);
            //获取主机名
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> files = config.getFiles(); //被监听的日志文件
        executorService = Executors.newFixedThreadPool(files.size());
        lastPositions = new HashMap<>();
        for(String file : files){
            lastPositions.put(file , 0L);
        }
    }


    //执行完构造方法后开始监听日志文件
    @PostConstruct
    public void startMonitor() {
        List<String> files = config.getFiles();
        for(String file : files) {
            Path path = Paths.get(file);
            executorService.execute(() -> {
                monitorLogFile(path);
            });
        }
    }

    /**
     * 读取日志文件的新增内容,并发送给RocketMQ
     * */
    @Override
    public void readAndReport(Path filePath) {
        try (RandomAccessFile file = new RandomAccessFile(filePath.toFile(), "r")) {
            Long lastPosition = lastPositions.get(filePath.toString());
            Long fileSize = Files.size(filePath);
            //日志文件新增了内容,进行读取操作
            if(lastPosition < fileSize) {
                file.seek(lastPosition);
                List<String> newLogs = new ArrayList<>();//新增日志
                String line;
                while((line = file.readLine()) != null){
                    //不读取空行
                    if (!line.isEmpty()) {
                        newLogs.add(line);
                    }
                }
                if(newLogs.size() > 0) {
                    Log log = new Log();
                    log.setHostName(hostName);
                    log.setFile(filePath.toString());
                    log.setLogs(newLogs);
                    System.out.println("send a message to " + LogServiceImpl.TOPIC);
                    Gson gson = new Gson();
                    String logGson = gson.toJson(log);
                    rocketMQTemplate.convertAndSend(LogServiceImpl.TOPIC, logGson);//发送消息至消息队列"HOST_LOG_TOPIC"主题
                }
            }

        } catch (IOException  e) {
            e.printStackTrace();
        }
    }

    // 使用 WatchService 监听日志文件变化
    @Override
    public void monitorLogFile(Path path) {
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
                        String relativePath = context.toString().substring(0 , context.toString().length()-1);
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

        } catch (IOException | InterruptedException e ) {
            e.printStackTrace();
        }
    }

}
