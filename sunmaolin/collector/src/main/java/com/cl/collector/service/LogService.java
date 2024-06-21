package com.cl.collector.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cl.collector.config.CFGConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.*;
import java.net.InetAddress;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * 日志监听上报
 *
 * @author: tressures
 * @date: 2024/6/14
 */
@Service
public class LogService {
    /**
     * 主机名
     */
    private String hostname;
    /**
     * 配置文件地址
     */
    private static final String CFG_CONFIG_PATH = "cfg.json";
    /**
     * upload URL
     */
    private static final String TO_SERVER_URL = "http://117.72.68.247:8888/api/log/upload";
    /**
     * 日志文件路径配置类
     */
    private CFGConfig cfgConfig;
    /**
     * 文件上一次的读取位置
     */
    private HashMap<String,Long> lastPositionMap;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private ReportService reportService;

    @PostConstruct
    public void initAndStartWatch(){
        try {
            hostname = InetAddress.getLocalHost().getHostName();
            ClassPathResource resource = new ClassPathResource(CFG_CONFIG_PATH);
            ObjectMapper objectMapper = new ObjectMapper();
            cfgConfig = objectMapper.readValue(resource.getInputStream(), CFGConfig.class);
            lastPositionMap = new HashMap<>();
            for (String path : cfgConfig.getFiles()){
                //初始化各个文件指针位置
                RandomAccessFile file = new RandomAccessFile(path, "r");
                file.seek(file.length());
                Long position = file.getFilePointer();
                lastPositionMap.put(path, position);
                //将任务加入线程池
                threadPoolTaskExecutor.execute(() -> watchLogFile(path));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 监听文件
     * @param path
     */
    public void watchLogFile(String path){
        try {
            // 指定要监听的目录
            Path dir = Paths.get(path);
            // 创建新的WatchService
            WatchService watchService = FileSystems.getDefault().newWatchService();
            // 注册监听事件(要监听的文件的父目录以及指定监听的事件类型)
            dir.getParent().register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
            while (true) {
                WatchKey key;
                try {
                    // 获取下一个WatchKey
                    key = watchService.take();
                }catch (InterruptedException e){
                    Thread.currentThread().interrupt(); // 恢复中断状态
                    return;
                }
                // 遍历所有事件
                for (WatchEvent<?> event : key.pollEvents()) {
                    // 获取事件类型
                    WatchEvent.Kind<?> kind = event.kind();
                    // 如果是ENTRY_MODIFY事件，则处理文件变化
                    if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                        // 获取事件上下文
                        WatchEvent<Path> ev = (WatchEvent<Path>) event;
                        Path filename = ev.context();
                        if(filename.toString().equals(dir.getFileName().toString())){
                            findNewLogs(dir.resolve(dir.toString()));
                        }
                    }
                }
                // 重置WatchKey
                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void findNewLogs(Path path){
        try {
            RandomAccessFile file = new RandomAccessFile(path.toFile(), "r");
            //获取对应的文件指针
            Long lastPosition = lastPositionMap.get(path.toString());
            //从上次读取的位置开始
            file.seek(lastPosition);
            String line;
            List<String> logs = new ArrayList<>();
            //读取新增的内容
            while((line = file.readLine()) != null){
                //去除两端换行符
                line = line.replaceAll("^\\n+|\\n+$", "");
                if("".equals(line)) continue;
                logs.add(line);
            }
            //更新文件指针
            lastPosition = file.getFilePointer();
            lastPositionMap.put(path.toString(),lastPosition);
            if(logs.size() > 0){
                upload(logs,path);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void upload(List<String> logs,Path path) {
        JSONArray logInfos = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("hostname",hostname);
        jsonObject.put("file",path.toString());
        jsonObject.put("logs",logs);
        logInfos.add(jsonObject);
        reportService.report(logInfos,TO_SERVER_URL);
    }
}
