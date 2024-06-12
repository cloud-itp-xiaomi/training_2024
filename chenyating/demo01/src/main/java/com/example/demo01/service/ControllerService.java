package com.example.demo01.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.NumberFormat;
import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;

public class ControllerService {


    //获取cpu利用率
    static double utilization=0.0;
    public static double getCpuStatus() throws FileNotFoundException{
        final var period = Duration.ofMinutes(1);
        new Timer().schedule(new CpuUtilizationTask(), 0, period.toMillis());
        return utilization;
    }
    static class CpuUtilizationTask extends TimerTask {
        private final String STAT_FILE_HEADER = "cpu  ";
        private final NumberFormat percentFormatter;
        private final RandomAccessFile statPointer;
        long previousIdleTime = 0, previousTotalTime = 0;

        public CpuUtilizationTask() throws FileNotFoundException {
            this.percentFormatter = NumberFormat.getPercentInstance();
            percentFormatter.setMaximumFractionDigits(2);
            var statFile = new File("/proc/stat");
            this.statPointer = new RandomAccessFile(statFile, "r");
        }

        @Override
        public void run() {
            try {
                var values = statPointer.readLine()
                        .substring(STAT_FILE_HEADER.length())
                        .split(" ");
                var idleTime = Long.parseUnsignedLong(values[3]);
                var totalTime = 0L;
                for (String value : values) {
                    totalTime += Long.parseUnsignedLong(value);
                }

                var idleTimeDelta = idleTime - previousIdleTime;
                var totalTimeDelta = totalTime - previousTotalTime;
                utilization = 1 - ((double) idleTimeDelta) / totalTimeDelta;

//                System.out.println(percentFormatter.format(utilization));

                previousIdleTime = idleTime;
                previousTotalTime = totalTime;

                statPointer.seek(0);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    //获取内存利用率

    static class MemUtilizationTask extends TimerTask {
        public MemUtilizationTask()throws FileNotFoundException {
            var memFile = new File("/proc/meminfo");
        }

        @Override
        public void run() {

        }
    }

}
