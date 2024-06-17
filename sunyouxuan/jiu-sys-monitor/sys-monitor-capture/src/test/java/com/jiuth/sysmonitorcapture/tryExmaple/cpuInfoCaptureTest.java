package com.jiuth.sysmonitorcapture.tryExmaple;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.NumberFormat;
import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;

public class cpuInfoCaptureTest
{
    @Test
    public void cpuInfoCaptureTest() throws FileNotFoundException {
        // 使用Duration类设置定时任务周期为1秒
        final var period = Duration.ofSeconds(1);
        new Timer().schedule(new CpuUtilizationTask(), 0, period.toMillis());
    }

    public static void main(String[] args) throws FileNotFoundException {

        // 使用Duration类设置定时任务周期为1秒
        final var period = Duration.ofSeconds(1);
        new Timer().schedule(new CpuUtilizationTask(), 0, period.toMillis());
    }
    /**
     * CpuUtilizationTask类继承了TimerTask类，用于定时获取CPU利用率
     */
    static class CpuUtilizationTask extends TimerTask {

        // /proc/stat文件的CPU信息行前缀
        private final String STAT_FILE_HEADER = "cpu  ";
        // 百分比格式化器
        private final NumberFormat percentFormatter;
        // 指向/proc/stat文件的指针
        private final RandomAccessFile statPointer;
        // 上一次的空闲时间
        private long previousIdleTime = 0;
        // 上一次的总时间
        private long previousTotalTime = 0;

        /**
         * 构造函数初始化百分比格式化器和文件指针
         */
        public CpuUtilizationTask() throws FileNotFoundException {
            this.percentFormatter = NumberFormat.getPercentInstance();
            percentFormatter.setMaximumFractionDigits(2);
            var statFile = new File("/proc/stat");
            /*通过使用RandomAccessFile，我们能够为保持打开的文件流
             *只要该对象存在，就不需要进一步打开文件*/
            this.statPointer = new RandomAccessFile(statFile, "r");
        }

        @Override
        public void run() {

            try {
                // 读取/proc/stat文件的第一行并分割成多个值
                String[] values = statPointer.readLine()
                        .substring(STAT_FILE_HEADER.length())
                        .split(" ");
                // 获取空闲时间
                long idleTime = Long.parseUnsignedLong(values[3]);
                long totalTime = 0L;
                for (String value : values) {
                    totalTime += Long.parseUnsignedLong(value);
                }

                // 计算时间差
                long idleTimeDelta = idleTime - previousIdleTime;
                long totalTimeDelta = totalTime - previousTotalTime;
                double utilization = 1 - ((double) idleTimeDelta) / totalTimeDelta;

                // 打印CPU利用率
                System.out.println(percentFormatter.format(utilization));

                // 更新上一次的时间值
                previousIdleTime = idleTime;
                previousTotalTime = totalTime;

                // 重置文件指针位置
                statPointer.seek(0);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }

}
