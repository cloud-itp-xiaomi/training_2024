package com.txh.xiaomi2024.work.collector.service.impl;

import com.txh.xiaomi2024.work.collector.service.UtilizationCollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.NumberFormat;
import java.util.regex.*;

/**
 * @author txh
 * 实现cpu和内存利用率的获取
 */
@Service
public class UtilizationCollectServiceImpl implements UtilizationCollectService {
    private final String STAT_FILE_HEADER = "cpu  ";
    private final NumberFormat percentFormatter;
    private final RandomAccessFile statPointer1;
    private final RandomAccessFile statPointer2;
    long previousIdleTime = 0, previousTotalTime = 0;

    @Autowired
    public UtilizationCollectServiceImpl() throws FileNotFoundException {
        this.percentFormatter = NumberFormat.getPercentInstance();
        percentFormatter.setMaximumFractionDigits(2);
        File statFile = new File("/proc/stat");
        File memFile = new File("/proc/meminfo");
        this.statPointer1 = new RandomAccessFile(statFile, "r");
        this.statPointer2 = new RandomAccessFile(memFile, "r");
    }
    @Override
    public Double getCpuUtilization() {
        try {
            String[] values = statPointer1.readLine()
                    .substring(STAT_FILE_HEADER.length())
                    .split(" ");

            long idleTime = Long.parseUnsignedLong(values[3]);
            long totalTime = 0;
            for (String value : values) {
                totalTime += Long.parseUnsignedLong(value);
            }

            // 当前减去前一次的
            long idleTimeDelta = idleTime - previousIdleTime;
            long totalTimeDelta = totalTime - previousTotalTime;
            double utilization = 1 - ((double) idleTimeDelta) / totalTimeDelta;

            System.out.println(percentFormatter.format(utilization));


            utilization = Math.round(utilization*100*100)/100.0;

            previousIdleTime = idleTime;
            previousTotalTime = totalTime;

            // take us back to the beginning of the file, so we don't have to reopen it
            statPointer1.seek(0);
            return utilization;
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return 0.0;
    }

    @Override
    public Double getMemoryUtilization() {
        try {

            // 匹配数字
            Pattern p = Pattern.compile("\\d+");

            long memTotal = 0;
            long memFree = 0;
            long buffer = 0;
            long cached = 0;

            String line = "";
            while ((line = statPointer2.readLine()) != null) {

                String[] parts = line.split(":");
                String key = parts[0].trim();

                if (key.equals("MemTotal")) {
                    Matcher m = p.matcher(parts[1].trim());
                    if(m.find()) {
                        memTotal = Long.parseUnsignedLong(m.group());
                    }
                } else if(key.equals("MemFree")) {
                    Matcher m = p.matcher(parts[1].trim());
                    if(m.find()) {
                        memFree = Long.parseUnsignedLong(m.group());
                    }
                } else if(key.equals("Buffers")) {
                    Matcher m = p.matcher(parts[1].trim());
                    if(m.find()) {
                        buffer = Long.parseUnsignedLong(m.group());
                    }
                } else if(key.equals("Cached")) {
                    Matcher m = p.matcher(parts[1].trim());
                    if(m.find()) {
                        cached = Long.parseUnsignedLong(m.group());
                    }
                }
            }
            long free = memFree + buffer + cached;
            double utilization = 1 - ((double) free) / memTotal;

            System.out.println(percentFormatter.format(utilization));

            utilization = Math.round(utilization*100*100)/100.0;

            // take us back to the beginning of the file, so we don't have to reopen it
            statPointer2.seek(0);
            return utilization;
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return 0.0;
    }

}
