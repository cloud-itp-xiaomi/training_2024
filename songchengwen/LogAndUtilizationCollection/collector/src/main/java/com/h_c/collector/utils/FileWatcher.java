package com.h_c.collector.utils;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.RandomAccessFile;

public class FileWatcher {
    private final List<String> files;
    private final String logStorage;
    private final Map<String, Long> fileLastModifiedMap = new ConcurrentHashMap<>();
    private final Map<String, Long> fileLastReadPositionMap = new ConcurrentHashMap<>();

    public FileWatcher(List<String> files, String logStorage) {
        this.files = files;
        this.logStorage = logStorage;
        initializeFileTracking();
    }

    private void initializeFileTracking() {
        for (String filePath : files) {
            Path path = Paths.get(filePath);
            try {
                fileLastModifiedMap.put(filePath, Files.getLastModifiedTime(path).toMillis());
                fileLastReadPositionMap.put(filePath, 0L);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        for (String filePath : files) {
            new Thread(() -> watchFile(filePath, logStorage)).start();
        }
    }

    private void watchFile(String filePath, String logStorage) {
        Path path = Paths.get(filePath);
        try {
            while (true) {
                long lastModifiedTime = Files.getLastModifiedTime(path).toMillis();
                if (lastModifiedTime > fileLastModifiedMap.get(filePath)) {
                    fileLastModifiedMap.put(filePath, lastModifiedTime);
                    List<String> newLogs = readNewLogs(path, filePath);
                    if (!newLogs.isEmpty()) {
                        sendLogsToServer(filePath, newLogs, logStorage);
                    }
                }
                Thread.sleep(5000);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private List<String> readNewLogs(Path path, String filePath) throws IOException {
        long lastReadPosition = fileLastReadPositionMap.get(filePath);
        List<String> newLogs = new ArrayList<>();
        try (RandomAccessFile reader = new RandomAccessFile(path.toFile(), "r")) {
            reader.seek(lastReadPosition);
            String line;
            while ((line = reader.readLine()) != null) {
                newLogs.add(line);
            }
            fileLastReadPositionMap.put(filePath, reader.getFilePointer());
        }
        return newLogs;
    }

    private void sendLogsToServer(String filePath, List<String> logs, String logStorage) {
        HttpClient.sendLogs(filePath, logs, logStorage);
    }
}
