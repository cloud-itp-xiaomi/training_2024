package com.collector.task;

import cn.hutool.core.util.NumberUtil;
import com.collector.bean.enums.MetricEnum;
import com.collector.bean.request.CollectorUploadRequest;
import com.collector.utils.Common;
import com.collector.service.ICollectorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@EnableScheduling
public class CollectorUploadTask {
    @Resource
    private ICollectorService iCollectorService;
    @Resource
    private Common common;

    // 每隔60s采集一次数据
    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void collectorUploadTask() {
        log.info("采集数据开始!");

        List<CollectorUploadRequest> requests = new ArrayList<>();
        try{
            // 获取当前时间戳,13位
            long timeMillis = System.currentTimeMillis();
            // cpu利用率
            double cpuUtilization = 0;
            // 内存利用率
            double memoryUtilization = 0;
            cpuUtilization = getCpuUtilization();
            memoryUtilization = getMemoryUtilization();
            String hostName = common.getHostName();

            CollectorUploadRequest request = new CollectorUploadRequest();
            request.setStep(60);
            request.setValue(cpuUtilization);
            request.setEndpoint(hostName);
            request.setTimestamp((int) timeMillis);
            request.setMetric(MetricEnum.CPU.getMessage());
            requests.add(request);

            request = new CollectorUploadRequest();
            request.setStep(60);
            request.setValue(memoryUtilization);
            request.setEndpoint(hostName);
            request.setTimestamp((int) timeMillis);
            request.setMetric(MetricEnum.MEM.getMessage());
            requests.add(request);

            iCollectorService.upload(requests);
        }catch (Exception e){
            e.printStackTrace();
            log.error("数据采集失败! " + e.getMessage());
        }
    }

    public double getCpuUtilization(){
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        CentralProcessor processor = hal.getProcessor();

        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(800);
        // 再次获取CPU使用时间，计算两次获取时间之间的差值，得到每个CPU状态（用户态、系统态、空闲态等）的使用时间
        long[] ticks = processor.getSystemCpuLoadTicks();
        // 低优先级进程占用CPU的时间
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        // 处理硬件中断所花费的时间
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        // 软件中断时间
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        // 在虚拟化环境中，表示虚拟CPU等待物理CPU时间片的时间
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        // CPU系统使用率
        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        // CPU用户使用率
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        // CPU当前等待率
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        // CPU当前空闲率
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        //CPU总的使用率
        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;

        return NumberUtil.round(NumberUtil.mul((float) (cSys+user) / totalCpu, 100), 1).doubleValue();
    }

    public static double getMemoryUtilization(){
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        GlobalMemory memory = hal.getMemory();

        long total = memory.getTotal();
        long used = total - memory.getAvailable();
        return NumberUtil.round(NumberUtil.mul((float) used / total, 100), 1).doubleValue();
    }
}
