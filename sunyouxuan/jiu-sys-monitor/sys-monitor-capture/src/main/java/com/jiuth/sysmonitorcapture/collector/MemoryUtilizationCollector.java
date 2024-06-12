package com.jiuth.sysmonitorcapture.collector;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


@Component
@Slf4j
public class MemoryUtilizationCollector {

    @Value("${meminfo.file.path}")
    private String meminfoFilePath;

    public double getMemUtilization() {
        try (BufferedReader meminfoReader = new BufferedReader(new FileReader(meminfoFilePath))) {
            String line;
            long totalMemory = 0;
            long availableMemory = 0;

            while ((line = meminfoReader.readLine()) != null) {
                if (line.startsWith("MemTotal:")) {
                    totalMemory = parseMemoryValue(line);
                } else if (line.startsWith("MemAvailable:")) {
                    availableMemory = parseMemoryValue(line);
                }
            }

            long usedMemory = totalMemory - availableMemory;
            double memoryUsage = (double) usedMemory / totalMemory * 100;

            // 输出详细的内存信息
            log.info("Total Memory: {} KB", totalMemory);
            log.info("Used Memory: {} KB", usedMemory);

            return memoryUsage;
        } catch (IOException e) {
            log.error("读取内存信息文件出错: {}", e.getMessage());
            return 0;
        }
    }

    private long parseMemoryValue(String line) {
        String[] parts = line.split("\\s+");
        return Long.parseLong(parts[1]);
    }
}