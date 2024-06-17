package com.jiuth.sysmonitorcapture.collector;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.RandomAccessFile;

@Slf4j
@Component
public class CpuUtilizationCollector {

    @Value("${cpu.stat.file.path}")
    private String cpuStatFilePath;

    private long previousIdleTime = 0;
    private long previousTotalTime = 0;
    private final String STAT_FILE_HEADER = "cpu  ";

    public double getCpuUtilization() {
        try (RandomAccessFile statFile = new RandomAccessFile(cpuStatFilePath, "r")) {
            String[] values = statFile.readLine().substring(STAT_FILE_HEADER.length()).split("\\s+");

            long idleTime = Long.parseLong(values[3]);
            long totalTime = 0;
            for (String value : values) {
                totalTime += Long.parseLong(value);
            }

            long idleTimeDelta = idleTime - previousIdleTime;
            long totalTimeDelta = totalTime - previousTotalTime;
            double utilization = 1 - ((double) idleTimeDelta) / totalTimeDelta;

            previousIdleTime = idleTime;
            previousTotalTime = totalTime;

            return utilization * 100;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }
}