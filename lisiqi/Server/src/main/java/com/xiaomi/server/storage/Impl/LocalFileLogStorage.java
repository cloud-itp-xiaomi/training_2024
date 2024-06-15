package com.xiaomi.server.storage.Impl;

import com.xiaomi.server.Entity.LogEntry;
import com.xiaomi.server.storage.LogStorage;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Component
public class LocalFileLogStorage implements LogStorage {
    private static final String STORAGE_PATH = "logs/";

    @Override
    public void store(List<LogEntry> logEntries) {
        for (LogEntry entry : logEntries) {
            try {
                String logFilePath = STORAGE_PATH + entry.getHostname() + "_" + entry.getFile().replace("/", "_") + ".log";
                Files.write(Paths.get(logFilePath), entry.getLogs());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<String> query(String hostname, String file) {
        try {
            String logFilePath = STORAGE_PATH + hostname + "_" + file.replace("/", "_") + ".log";
            return Files.readAllLines(Paths.get(logFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
