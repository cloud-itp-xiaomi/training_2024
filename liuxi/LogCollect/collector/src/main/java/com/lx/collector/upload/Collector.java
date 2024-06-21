package com.lx.collector.upload;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lx.collector.pojo.Utilization;
import com.lx.collector.service.CollectService;
import com.lx.collector.service.impl.CollectServiceImpl;
import com.lx.collector.utils.GetBeanUtil;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@DependsOn(value = "getBeanUtil")
public class Collector {

    private RocketMQTemplate rocketMQTemplate = GetBeanUtil.getBean(RocketMQTemplate.class);
    private CollectService collectService = GetBeanUtil.getBean(CollectServiceImpl.class);
    private static final String TOPIC = "HOST_UTILIZATION_TOPIC";
    private Long count = 0L;//发送消息总数

    @Scheduled(fixedRate = 60000) // 每分钟执行一次
    public void collectAndReport() {

        Utilization cpuUtilization = new Utilization();
        Utilization memUtilization = new Utilization();

        //获取主机名
        String hostName = getHostName();

        //去掉年份获取系统当前时间，希望两条数据以同一时间录入
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddhhmmss");
        String format  = LocalDateTime.now().format(formatter);
        Long curTime = Long.parseLong(format);

        //采集数据
        double cpuUtilizationValue = collectService.collectCPU( "E:\\java_study\\javaProjects\\LogCollect\\collector\\src\\main\\java\\com\\lx\\collector\\upload\\meminfo");
        double memUtilizationValue = collectService.collectMem("E:\\java_study\\javaProjects\\LogCollect\\collector\\src\\main\\java\\com\\lx\\collector\\upload\\meminfo");

        //cpu利用率
        cpuUtilization.setMetric("cpu.used.percent");
        cpuUtilization.setEndpoint(hostName);
        cpuUtilization.setTimestamp(curTime);
        cpuUtilization.setValue(cpuUtilizationValue);

        //内存利用率
        memUtilization.setMetric("mem.used.percent");
        memUtilization.setEndpoint(hostName);
        memUtilization.setTimestamp(curTime);
        memUtilization.setValue(memUtilizationValue);

        //发送消息给消息队列
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String cpuJson = objectMapper.writeValueAsString(cpuUtilization);
            String memJson = objectMapper.writeValueAsString(cpuUtilization);
            rocketMQTemplate.convertAndSend(Collector.TOPIC, cpuJson);
            rocketMQTemplate.convertAndSend(Collector.TOPIC, memJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        count += 2 ;
        System.out.println("The collector has sent " + count + " messages to  " + Collector.TOPIC + " ~");
    }

    private String getHostName(){
        String hostName = null;
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return hostName;
    }
}
