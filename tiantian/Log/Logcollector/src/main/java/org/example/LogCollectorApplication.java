package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import org.example.entity.LogFile;
import org.example.factory.LogUploaderFactory;
import org.example.uploader.LogUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;

import java.util.*;


@SpringBootApplication
@EnableScheduling
public class LogCollectorApplication {

  @Autowired
  private LogUploaderFactory logUploaderFactory;
  // 监听
  private WatchService watcher;
  // 存监控的目录的WatchKey与对应的Path
  private Map<WatchKey, Path> keys;
  private Map<Path, List<String>> lastKnownContents = new HashMap<>();

  private String logStorage;

  public static void main(String[] args) {
    SpringApplication.run(LogCollectorApplication.class, args);
  }


  public LogCollectorApplication() throws Exception {
    this.watcher = FileSystems.getDefault().newWatchService();
    this.keys = new HashMap<>();
    // 初始化
    initWatchService();
  }

  private void initWatchService() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};
    Map<String, Object> config = mapper.readValue(new File("Logcollector/src/main/resources/cfg.json"), typeRef);
    List<String> files = (List<String>) config.get("files");
    this.logStorage = (String) config.get("log_storage");

    for (String file : files) {
      Path path = Paths.get(file).getParent();
      if (path != null && !keys.containsKey(path)) {
        // 监听文件创建、删除和修改事件
        WatchKey key = path.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        keys.put(key, path);
        Path fullPath = path.resolve(file);
        lastKnownContents.put(fullPath, Files.readAllLines(fullPath));
      }
    }
  }

  // 5s执行一次
  @Scheduled(fixedRate = 5000)
  public void processEvents() {
    WatchKey key;
    while ((key = watcher.poll()) != null) {
      Path dir = keys.get(key);
      for (WatchEvent<?> event : key.pollEvents()) {
        WatchEvent.Kind<?> kind = event.kind();

        @SuppressWarnings("unchecked")
        WatchEvent<Path> ev = (WatchEvent<Path>) event;
        Path name = ev.context();
        Path child = dir.resolve(name);

        System.out.println(event.kind().name() + ": " + child);

        // 对于每个修改事件，比较旧内容和新内容，提取新增行
        if (kind == ENTRY_MODIFY && lastKnownContents.containsKey(child)) {
          try {
            List<String> newContents = Files.readAllLines(child);
            List<String> oldContents = lastKnownContents.get(child);
            List<String> addedLines = new ArrayList<>();

            Set<String> oldLines = new HashSet<>(oldContents);
            for (String line : newContents) {
              if (!oldLines.contains(line)) {
                addedLines.add(line);
              }
            }

            if (!addedLines.isEmpty()) {
              LogFile logFile = new LogFile("localhost", child.toString(), addedLines.toArray(new String[0]));
              List<LogFile> logFiles = new ArrayList<>();
              logFiles.add(logFile);
              LogUploader uploader = logUploaderFactory.createLogUploader(logStorage);
              uploader.uploadFiles(logFiles);
            }

            // 更新content
            lastKnownContents.put(child, newContents);
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
      key.reset();
    }
  }



}
