package com.example.xiaomi1coll.collector;

import com.example.xiaomi1coll.entity.Metric;
import com.sun.management.OperatingSystemMXBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.math.RoundingMode;

import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@Component
public class Collector {
    private static final Logger logger = LoggerFactory.getLogger(Collector.class);
    private final RestTemplate restTemplate;
    private final OperatingSystemMXBean osBean;
    private final ExecutorService executorService;

    public Collector() {
        // 创建自定义的 RestTemplate，并设置请求超时
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000); // 设置连接超时时间（毫秒）
        requestFactory.setReadTimeout(5000); // 设置读取超时时间（毫秒）
        this.restTemplate = new RestTemplate(requestFactory);

        this.osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        // 创建一个包含4个线程的线程池
        this.executorService = Executors.newFixedThreadPool(4);
    }

    public Collector(RestTemplate restTemplate, OperatingSystemMXBean osBean, ExecutorService executorService) {
        this.restTemplate = restTemplate;
        this.osBean = osBean;
        this.executorService = executorService;
    }

    @Scheduled(fixedRate = 60000) // 每分钟执行一次
    // @Scheduled(fixedRate = 3000) // 测试，3秒一次
    public void collectAndReport() {
        double cpuLoad = getCpuUsage(); // 获取系统的平均负载（过去1分钟）
        double memoryUsage = getMemoryUsage(); // 获取内存使用情况

        Metric cpuMetric = createMetric("cpu.used.percent", cpuLoad);
        Metric memMetric = createMetric("mem.used.percent", memoryUsage);

        // 提交任务到线程池
        submitMetric(cpuMetric);
        submitMetric(memMetric);

        System.out.println("cpu采集日志："+cpuMetric);
        System.out.println("mem采集日志："+memMetric);

    }

    // 创建主机指标类
    private Metric createMetric(String metricName, double value) {
        Metric metric = new Metric();
        metric.setMetric(metricName);
        metric.setEndpoint("my-computer");
        metric.setTimestamp(Instant.now().getEpochSecond());
        metric.setStep(60);
        metric.setValue(value);
        return metric;
    }

    // 线程调用
    private void submitMetric(Metric metric) {
        // 提交任务到执行服务
        executorService.submit(() -> sendMetric(metric));
    }

    // 上报数据
    public void sendMetric(Metric metric) {
        // 构造HTTP请求
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Metric> requestEntity = new HttpEntity<>(metric, headers);

        try {
            // 发送POST请求到服务器的API接口
            ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/api/metric/upload",
                    HttpMethod.POST, requestEntity, String.class);

            // 记录响应的状态码和内容
            logger.info("Metric sent: {}", metric);
            logger.info("Response Status: {}", response.getStatusCode());
            logger.info("Response Body: {}", response.getBody());
        } catch (ResourceAccessException e) {
            // 处理超时异常
            logger.error("Request timeout when sending metric: {}", metric, e);
        } catch (Exception e) {
            // 处理其他异常
            logger.error("Error sending metric: {}", metric, e);
        }
    }


    // 获取主机内存使用率
    public double getMemoryUsage() {
        long totalMemory = osBean.getTotalMemorySize();
        long freeMemory = osBean.getFreeMemorySize();

        if (totalMemory == 0) {
            // 返回无效数据
            return -1;
        }
        return (double) (totalMemory - freeMemory) / totalMemory * 100;
    }


    // 获取主机cpu使用率
    public double getCpuUsage() {
        double systemCpuLoad = osBean.getCpuLoad();
        if (systemCpuLoad < 0) {
            return Double.NaN;
        }
        // return twoDecimal(systemCpuLoad * 100); // 转换为百分比并保留两位小数
        return (double) Math.round(systemCpuLoad * 10000) / 100.0; // 转换为百分比%，并保留小数点后两位
    }

    // 控制精度
    public double twoDecimal(double doubleValue) {
        BigDecimal bigDecimal = new BigDecimal(doubleValue).setScale(2, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }


}
