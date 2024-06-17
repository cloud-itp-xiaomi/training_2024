package org.qiaojingjing.collector;

import org.qiaojingjing.constants.Param;
import org.qiaojingjing.entity.Metric;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CollectCpu {
    private static final Metric cpu = new Metric();
    private static Long[] lastValues = null;

    public static Metric collect() throws IOException {
        String hostname = Files.readString(Paths.get("/etc/hostname"))
                                                .trim();
        String statLine = getFirstLine();
        Long[] values = string2Long(statLine);

        if (lastValues != null) {
            Long currentIdleTime = values[3];
            Long previousIdleTime = lastValues[3];
            Long currentTotalTime = getSum(values);
            Long previousTotalTime = getSum(lastValues);
            long idleTime = currentIdleTime - previousIdleTime;
            long totalTime = currentTotalTime - previousTotalTime;
            double cpuUsage = (double) 100 * (totalTime - idleTime) / totalTime;
            BigDecimal cpuUsageParse2 = new BigDecimal(cpuUsage);
            cpuUsage = cpuUsageParse2.setScale(2, RoundingMode.HALF_UP)
                                     .doubleValue();

            cpu.setEndpoint(hostname);
            cpu.setStep(60L);
            cpu.setMetric(Param.CPU);
            cpu.setValue(cpuUsage);
            cpu.setTags(null);

            return cpu;
        } else {
            lastValues = values;

            return null;
        }

    }

    private static String getFirstLine() throws IOException {
        File statFile = new File("/proc/stat");
        FileInputStream inputStream = new FileInputStream(statFile);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        return br.readLine();
    }
    public static Long[] getCurrentCpuValues() throws IOException {
        String line = getFirstLine();

        return string2Long(line);
    }

    private static Long getSum(Long[] values) {
        Long totalCpuTime = 0L;
        for (Long value : values) {
            totalCpuTime += value;
        }

        return totalCpuTime;
    }

    private static Long[] string2Long(String line) {
        String[] valueStr = line.split("\\s+");
        Long[] values = new Long[valueStr.length-1];
        for (int i = 0; i < values.length; i++) {
            values[i] = Long.parseLong(valueStr[i+1]);
        }

        return values;
    }
}
