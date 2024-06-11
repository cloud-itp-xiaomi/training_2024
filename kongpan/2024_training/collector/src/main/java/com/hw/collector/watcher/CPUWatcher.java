package com.hw.collector.watcher;

import org.springframework.stereotype.Component;

import java.io.*;

/**
 * @author mrk
 * @create 2024-05-21-16:31
 */
@Component
public class CPUWatcher {

    private final String STAT_FILE_HEADER = "cpu  ";
//    private final String FILE_PATH = "/proc/stat";
    private final String FILE_PATH = "D:/files/mi/stat";

    private long previousIdleTime = 0, previousTotalTime = 0;

    public double getCpuUsage() throws IOException {
        String[] cpuTimes = getCpuTimes(FILE_PATH);

        long idleTime = Long.parseUnsignedLong(cpuTimes[3]);
        long totalTime = getTotalTime(cpuTimes);

        long idleTimeDelta = idleTime - previousIdleTime;
        long totalTimeDelta = totalTime - previousTotalTime;

        previousIdleTime = idleTime;
        previousTotalTime = totalTime;

        return (1.0 - idleTimeDelta / (double) totalTimeDelta) * 100;
    }

    /**
     * 读取 "/proc/stat"文件 获取 "cpu " 开头行的 cpu 各类型时间
     * @param filePath 读取文件路径
     * @return
     * @throws IOException
     */
    private String[] getCpuTimes(String filePath) throws IOException {
        try {
            RandomAccessFile statPointer = new RandomAccessFile(
                    new File(filePath), "r");

            return statPointer.readLine()
                    .substring(STAT_FILE_HEADER.length())
                    .split("\\s+");
        } catch (IOException e) {
            throw new IOException("Unable to read /proc/stat!!!");
        }
    }

    private long getTotalTime(String[] cpuTimes) {
        long totalTime = 0;
        for (String cpuTime : cpuTimes) {
            totalTime += Long.parseUnsignedLong(cpuTime);
        }
        return totalTime;
    }
}
