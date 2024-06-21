package com.example.springcloud.base;

import cn.hutool.system.oshi.CpuInfo;
import cn.hutool.system.oshi.OshiUtil;
import org.springframework.stereotype.Component;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import java.text.DecimalFormat;

/**
 * @ClassName SystemInfoUtils
 * @Description 系统消息工具类
 * @Author 石心木PC
 * @create: 2024-06-06 14:14
 **/
@Component
public class SystemInfoUtils {
    private static final int OSHI_WAIT_SECOND = 1000;
    private static final SystemInfo systemInfo = new SystemInfo();
    final DecimalFormat format = new DecimalFormat("#.0");
    private static final HardwareAbstractionLayer hardware = systemInfo.getHardware();
    private static final OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
    /**
     * 获取CPU信息
     */
    public float getCpuInfo() {
        CentralProcessor processor = hardware.getProcessor();
        // CPU信息
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(OSHI_WAIT_SECOND);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
        return Float.parseFloat(format.format((totalCpu - idle) * 1.0 / totalCpu * 100));
    }
    /**
     * 获取内存信息
     */
    public float getMemInfo() {
        long total = hardware.getMemory().getTotal();
        long available = hardware.getMemory().getAvailable();
        return Float.parseFloat(format.format(((total - available) * 1.0 / total)*100));
    }

    public float getOsInfo(){
        CpuInfo cpuInfo = OshiUtil.getCpuInfo();
        double free = cpuInfo.getFree();
        return Float.parseFloat(format.format(100.0f - free));
    }
    public String getHostName(){
        return operatingSystem.getNetworkParams().getHostName();
    }

}
