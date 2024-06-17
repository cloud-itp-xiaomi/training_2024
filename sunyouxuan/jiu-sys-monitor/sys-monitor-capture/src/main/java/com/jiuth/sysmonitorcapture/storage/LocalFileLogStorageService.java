package com.jiuth.sysmonitorcapture.storage;


import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class LocalFileLogStorageService implements LogStorageService {

    @Override
    public void storeLogs(Map<String, List<String>> fileLogs) throws IOException {
        for (Map.Entry<String, List<String>> entry : fileLogs.entrySet()) {
            File logFile = new File("stored_logs/" + entry.getKey().replaceAll("[^a-zA-Z0-9.-]", "_") + ".log");
            logFile.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(logFile, true)) {
                for (String log : entry.getValue()) {
                    writer.write(log + System.lineSeparator());
                }
            }
        }
    }
}
