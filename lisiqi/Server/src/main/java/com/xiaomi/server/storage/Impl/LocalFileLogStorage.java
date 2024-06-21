package com.xiaomi.server.storage.Impl;

import com.xiaomi.server.Entity.LogEntry;
import com.xiaomi.server.storage.LogStorage;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class LocalFileLogStorage implements LogStorage {
    private static final String LOG_DIRECTORY = "D:/2024_training/localfileStorage/";

    @PostConstruct
    public void init() {
        File directory = new File(LOG_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }


    @Override
    public void saveLogEntries(List<LogEntry> logEntries) {
        for (LogEntry logEntry : logEntries) {
            try {
                String sanitizedFilePath = sanitizeFileName(logEntry.getFile());

                String filePath = Paths.get(LOG_DIRECTORY, logEntry.getHostname() + "_" + sanitizedFilePath).toString();
                try (FileWriter fw = new FileWriter(filePath, true);
                     BufferedWriter bw = new BufferedWriter(fw);
                     PrintWriter out = new PrintWriter(bw)) {
                    for (String log : logEntry.getLogs()) {
                        out.println(log);
                    }
                }
                System.out.println("Log entries saved to " + filePath);  // 打印成功信息
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public List<LogEntry> getLogEntries(String hostname, String file) {
        List<LogEntry> logEntries = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(LOG_DIRECTORY + hostname + "_" + file))) {
            String line;
            while ((line = br.readLine()) != null) {
                LogEntry logEntry = new LogEntry();
                logEntry.setHostname(hostname);
                logEntry.setFile(file);
                logEntry.setLogs(Collections.singletonList(line));
                logEntries.add(logEntry);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return logEntries;
    }

    private String sanitizeFileName(String fileName) {
        // 将路径分隔符转换为下划线并替换其他不合法字符
        return fileName.replace("\\", "_").replace("/", "_").replaceAll("[\\:*?\"<>|]", "_");
    }


}

