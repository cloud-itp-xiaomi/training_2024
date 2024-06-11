package org.example.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;
import java.io.IOException;
import java.util.*;

import org.example.entity.LogFile;
import org.example.factory.LogUploaderFactory;
import org.example.uploader.LogUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;

@Component
public class LogEventWatcher {

  @Autowired
  private LogUploaderFactory logUploaderFactory;

  private WatchService watcher;
  private Map<WatchKey, Path> keys;
  private Map<Path, List<String>> lastKnownContents = new HashMap<>();
  private String logStorage;

  public LogEventWatcher() throws Exception {
    this.watcher = FileSystems.getDefault().newWatchService();
    this.keys = new HashMap<>();
    initWatchService();
    startWatching();
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
        WatchKey key = path.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        keys.put(key, path);
        Path fullPath = path.resolve(file);
        lastKnownContents.put(fullPath, Files.readAllLines(fullPath));
      }
    }
  }

  private void startWatching() {
    Thread thread = new Thread(() -> {
      while (true) {
        processEvents();
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          System.out.println("File watching thread was interrupted");
          break;
        }
      }
    });
    thread.start();
  }

  private void processEvents() {
    WatchKey key;
    while ((key = watcher.poll()) != null) {
      Path dir = keys.get(key);
      for (WatchEvent<?> event : key.pollEvents()) {
        processEvent(event, dir);
        key.reset();
      }
    }
  }

  private void processEvent(WatchEvent<?> event, Path dir) {
    WatchEvent.Kind<?> kind = event.kind();
    WatchEvent<Path> ev = (WatchEvent<Path>) event;
    Path name = ev.context();
    Path child = dir.resolve(name);
    System.out.println(event.kind().name() + ": " + child);

    // 处理逻辑
    if (kind == ENTRY_MODIFY && lastKnownContents.containsKey(child)) {
      try {
        updateFileContents(child);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void updateFileContents(Path child) throws IOException {
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

    lastKnownContents.put(child, newContents);
  }
}