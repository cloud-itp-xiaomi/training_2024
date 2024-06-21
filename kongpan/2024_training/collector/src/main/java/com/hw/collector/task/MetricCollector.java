package com.hw.collector.task;

import com.hw.collector.client.MetricClient;
import com.hw.collector.dto.Metrics;
import com.hw.collector.utils.CollectUtil;
import com.hw.collector.watcher.CPUWatcher;
import com.hw.collector.watcher.MemoryWatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author mrk
 * @create 2024-05-21-15:30
 */
@Component
@RequiredArgsConstructor
public class MetricCollector {

    private final MetricClient metricClient;
    private final CollectUtil collectUtil;

    @Scheduled(fixedRate = 60000)
    private void collectAndSendMetrics() throws IOException {
        CPUWatcher cpuWatcher = new CPUWatcher();
        MemoryWatcher memoryWatcher = new MemoryWatcher();
        double cpuUsage = formatUsage(cpuWatcher.getCpuUsage());
        double memoryUsage = formatUsage(memoryWatcher.getMemoryUsage());

        String ip = collectUtil.getIpAddress();

        List<Metrics> metrics = new ArrayList<>();

        Metrics cpuMetric = new Metrics();
        cpuMetric.setEndpoint(ip);
        cpuMetric.setMetric("cpu.used.percent");
        cpuMetric.setTimestamp(System.currentTimeMillis() / 1000);
        cpuMetric.setStep(60L);
        cpuMetric.setValue(cpuUsage);

        Metrics memoryMetric = new Metrics();
        memoryMetric.setEndpoint(ip);
        memoryMetric.setMetric("mem.used.percent");
        memoryMetric.setTimestamp(System.currentTimeMillis() / 1000);
        memoryMetric.setStep(60L);
        memoryMetric.setValue(memoryUsage);

        metrics.add(cpuMetric);
        metrics.add(memoryMetric);

        metricClient.uploadMetrics(metrics);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        System.out.println("send request time: " + simpleDateFormat.format(date));
//        System.out.println("cpu usage: " + cpuUsage + " memory usage: " + memoryUsage);
    }

    public static double formatUsage(double number) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(1);
        String str = numberFormat.format(number);
        return Double.parseDouble(str);
    }
}
