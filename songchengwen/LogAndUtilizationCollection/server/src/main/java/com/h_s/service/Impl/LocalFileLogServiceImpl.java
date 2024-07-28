package com.h_s.service.Impl;

import com.h_s.service.LogService;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class LocalFileLogServiceImpl implements LogService {
    private static final String LOCAL_LOG_DIR = "E:\\WorkSpace\\IDEA Workspace\\UtilizationCollection\\server\\src\\main\\java\\com\\h_s\\updateLog\\";

    @Override
    public void saveLogs(String hostname, String file, List<String> logs) {
        Path filePath = Paths.get(file);
        String fileName = filePath.getFileName().toString();
        String fullPath = LOCAL_LOG_DIR + fileName;
        try {
            Files.createDirectories(Paths.get(LOCAL_LOG_DIR));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileWriter writer = new FileWriter(fullPath, true)) {
            for (String log : logs) {
                writer.write(log + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Map<String, String>> queryLogs(String hostname, String file) {

        return Collections.emptyList();
    }
}
