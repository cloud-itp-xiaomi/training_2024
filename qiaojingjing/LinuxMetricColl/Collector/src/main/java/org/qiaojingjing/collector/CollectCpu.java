package org.qiaojingjing.collector;

import org.qiaojingjing.cons.Param;
import org.qiaojingjing.entity.Metric;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CollectCpu {
    private static final Metric cpu = new Metric();
    // 存储上一次读取的CPU指标值
    private static Long[] lastValues = null;

    public static Metric collect() throws IOException {
        String hostname = Files.readString(Paths.get("/etc/hostname")).trim();

        // 读取当前的CPU指标值
        String statLine = getFirstLine();
        Long[] values = string2Long(statLine);

        if (lastValues != null) {
            // 计算CPU使用率
            Long currentIdleTime = values[3];
            Long previousIdleTime = lastValues[3];
            Long currentTotalTime = getSum(values);
            Long previousTotalTime = getSum(lastValues);
            long idleTime = currentIdleTime - previousIdleTime;
            long totalTime = currentTotalTime - previousTotalTime;
            double cpuUsage = (double) 100 * (totalTime - idleTime) / totalTime;
            // 转为BigDecimal保留2位小数
            BigDecimal cpuUsageParse2 = new BigDecimal(cpuUsage);
            cpuUsage = cpuUsageParse2.setScale(2, RoundingMode.HALF_UP).doubleValue();

            cpu.setEndpoint(hostname);
            cpu.setStep(60L);
            cpu.setMetric(Param.CPU);
            cpu.setValue(cpuUsage);
            cpu.setTags(null);

            return cpu;
        } else {
            // 把上一次的值存进去
            lastValues = values;
            return null;
        }

    }

    private static String getFirstLine() throws IOException {
        File statFile = new File("/proc/stat");
        FileInputStream inputStream = new FileInputStream(statFile);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        //只需要取第一行的参数即可
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
        //对line进行加工，读取相应值
        String[] valueStr = line.split("\\s+");
        Long[] values = new Long[valueStr.length-1];//存放数字
        for (int i = 0; i < values.length; i++) {
            values[i] = Long.parseLong(valueStr[i+1]);
        }
        return values;
    }


}
