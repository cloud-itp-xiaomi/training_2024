package com.jiuth.sysmonitorcapture;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;

public class memoryInfoCaptureTest {
    @Test
    void memoryInfoCaptureTest() throws FileNotFoundException {
        // 使用Duration类设置定时任务周期为1秒
        final var period = Duration.ofSeconds(1);
        new Timer().schedule(new memoryInfoCaptureTest.MemoryUtilizationTask(), 0, period.toMillis());
    }

    public static void main(String[] args) throws FileNotFoundException {
        // 使用Duration类设置定时任务周期为1秒
        final var period = Duration.ofSeconds(1);
        new Timer().schedule(new memoryInfoCaptureTest.MemoryUtilizationTask(), 0, period.toMillis());
    }

    /**
     * MemoryUtilizationTask类继承了TimerTask类，用于定时获取内存利用率
     */
    static class MemoryUtilizationTask extends TimerTask {

        // 内存信息文件路径
        private final String MEMINFO_FILE = "/proc/meminfo";
        // 百分比格式化器
        private final NumberFormat percentFormatter;
        // BufferedReader用于读取内存信息文件
        private BufferedReader meminfoReader;

        /**
         * 构造函数初始化百分比格式化器和BufferedReader
         */

        public MemoryUtilizationTask() throws FileNotFoundException {
            this.percentFormatter = NumberFormat.getPercentInstance();
            percentFormatter.setMaximumFractionDigits(2);
            this.meminfoReader = new BufferedReader(new FileReader(MEMINFO_FILE));
        }

        @Override
        public void run() {
            try {
                String line;
                long totalMemory = 0;
                long availableMemory = 0;

                // 逐行读取/proc/meminfo文件，提取总内存和可用内存信息
                while ((line = meminfoReader.readLine()) != null) {
                    if (line.startsWith("MemTotal:")) {
                        totalMemory = parseMemoryValue(line);
                    } else if (line.startsWith("MemAvailable:")) {
                        availableMemory = parseMemoryValue(line);
                    }
                }

                long usedMemory = totalMemory - availableMemory;
                double memoryUsage = (double) usedMemory / totalMemory;

                // 输出总内存、已用内存和内存使用率百分比
                System.out.println("Total Memory: " + totalMemory + " KB");
                System.out.println("Used Memory: " + usedMemory + " KB");
                System.out.println("Memory Usage: " + percentFormatter.format(memoryUsage));

                // 重置BufferedReader到文件开头
                meminfoReader.close();
                this.meminfoReader = new BufferedReader(new FileReader(MEMINFO_FILE));

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

        /**
         * 解析内存值
         * @param line 包含内存信息的行
         * @return 解析后的内存值（以KB为单位）
         */
        private long parseMemoryValue(String line) {
            String[] parts = line.split("\\s+");
            return Long.parseLong(parts[1]);
        }
    }

}
