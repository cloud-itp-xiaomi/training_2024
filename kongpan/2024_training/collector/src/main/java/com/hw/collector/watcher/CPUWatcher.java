package com.hw.collector.watcher;

import java.io.*;

/**
 * @author mrk
 * @create 2024-05-21-16:31
 */
public class CPUWatcher {

    private final String STAT_FILE_HEADER = "cpu  ";

    private long previousIdleTime = 0, previousTotalTime = 0;

    public double getCpuUsage() throws IOException {
        String[] cpuTimes = getCpuTimes();

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
     * @return
     * @throws IOException
     */
    private String[] getCpuTimes() throws IOException {
        try {
            RandomAccessFile statPointer = new RandomAccessFile(
                    new File("/proc/stat"), "r");

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
