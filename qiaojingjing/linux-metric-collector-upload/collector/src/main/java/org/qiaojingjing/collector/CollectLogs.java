package org.qiaojingjing.collector;

import lombok.extern.slf4j.Slf4j;
import org.qiaojingjing.config.LogConfig;
import org.qiaojingjing.entity.Log;
import org.qiaojingjing.utils.POSTRequestUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 功能：增量上传日志。
 * 为每个日志路径开启一条线程，设置一个WatchService服务负责监听文件变化
 * 每个日志维护一个Log对象，避免每次文件变化时就new一个log对象。
 * 使用存储在redis中的变量记录上次读取到的位置
 * dirContentsMap: 存储每个目录与其对应的contents列表的映射
 * logMap: 存储每个文件路径与其对应的Log对象的映射
 * @version 0.1.0
 * @author qiaojingjing
 * @since 0.1.0
 **/

@Service
@Slf4j
public class CollectLogs {
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    Map<String, List<String>> dirContentsMap = new ConcurrentHashMap<>();
    Map<String, Log> logMap = new ConcurrentHashMap<>();

    public Integer getLastLine(String path) {
        String lastLineString = redisTemplate.opsForValue().get(path);
        return Integer.parseInt(lastLineString);
    }

    public void setLastLine(String path, Integer currentLine) {
        redisTemplate.opsForValue().set(path, String.valueOf(currentLine));
    }

    @PostConstruct
    public void collect() throws IOException {
        List<String> filesPathList = LogConfig.getFilesPathList();
        for (String filePath : filesPathList) {
            log.info("监听的文件为" + filePath);
            Path logDir = Paths.get(filePath);
            WatchService watchService = FileSystems.getDefault().newWatchService();
            logDir.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
            dirContentsMap.put(filePath, new ArrayList<>());
            new Thread(() -> watchFileChanges(logDir,
                                              watchService,
                                              filePath)).start();
        }
    }
//事务 强事务
    private void watchFileChanges(Path logDir,
                                  WatchService watchService,
                                  String filePath) {
        List<String> contents = dirContentsMap.get(filePath);
        var ref = new Object() {
            Integer currentLastLine;
        };
        while (true) {
            try {
                WatchKey key = watchService.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                        Path changedFile = (Path) event.context();
                        Path fullPath = logDir.resolve(changedFile);
                        String fullPathString = String.valueOf(fullPath);
                        log.info("文件:{}被修改了", fullPath);

                        Log log2collect = logMap.computeIfAbsent(fullPathString, k -> new Log());
                        log2collect.setHostname("my-computer");
                        log2collect.setFile(fullPathString);

                        try (BufferedReader reader = new BufferedReader(new FileReader(fullPathString))){
                            String line;
                            ref.currentLastLine = getLastLine(fullPathString);

                            for (int i = 0; i < ref.currentLastLine - 1; i++) {
                                line = reader.readLine();
                                if (line == null) {
                                    break;
                                }
                            }
                            while ((line = reader.readLine()) != null) {
                                contents.add(line);
                                ref.currentLastLine++;
                            }
                            setLastLine(fullPathString, ref.currentLastLine);
                            reader.close();
                            log2collect.setLogs(contents);
                        } catch (IOException e) {
                            log.error("An I/O error occurred while watching file changes", e);
                        }
                    }
                }
                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            } catch (InterruptedException e) {
                log.error("The watch thread was interrupted");
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    @Scheduled(fixedRate = 30000, initialDelay = 5000)
    public void uploader() {
        log.info("准备上传日志");
        Log[] logArray = logMap.values().toArray(new Log[0]);
        boolean isEmpty = false;
        if(logArray.length == 0){
            isEmpty = true;
            log.info("没有新日志，不上传");
        }
        if(!isEmpty){
            POSTRequestUtil.sendLogsPostRequest(logArray);
            log.info("上传完成");
        }
        for (Map.Entry<String, List<String>> entry : dirContentsMap.entrySet()) {
            entry.getValue().clear();
        }
    }
}