package com.example.demo01.bean;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.CentralProcessor.TickType;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.util.Util;

public class SysServer {

    private static final int OSHI_WAIT_SECOND = 1000;

    // CPU相关信息
    private Cpu cpu = new Cpu();

    //內存相关信息
    private Mem mem = new Mem();

    public Cpu getCpu() {
        return cpu;
    }

    public void setCpu(Cpu cpu) {
        this.cpu = cpu;
    }

    public Mem getMem() {
        return mem;
    }

    public void setMem(Mem mem) {
        this.mem = mem;
    }

    public void copyTo() {
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        setCpuInfo(hal.getProcessor());
        setMemInfo(hal.getMemory());
    }

    //设置CPU信息
    private void setCpuInfo(CentralProcessor processor) {
        // CPU信息
        //获取当前CPU的总使用时间，并休眠一定时间（OSHI_WAIT_SECOND秒）。
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(OSHI_WAIT_SECOND);

        //再次获取CPU使用时间，计算两次获取时间之间的差值，得到每个CPU状态（用户态、系统态、空闲态等）的使用时间
        long[] ticks = processor.getSystemCpuLoadTicks();
        //低优先级进程占用CPU的时间
        long nice = ticks[TickType.NICE.getIndex()] - prevTicks[TickType.NICE.getIndex()];
        //处理硬件中断所花费的时间
        long irq = ticks[TickType.IRQ.getIndex()] - prevTicks[TickType.IRQ.getIndex()];
        //软件中断时间
        long softirq = ticks[TickType.SOFTIRQ.getIndex()] - prevTicks[TickType.SOFTIRQ.getIndex()];
        //在虚拟化环境中，表示虚拟CPU等待物理CPU时间片的时间
        long steal = ticks[TickType.STEAL.getIndex()] - prevTicks[TickType.STEAL.getIndex()];

        //系统CPU时间
        long cSys = ticks[TickType.SYSTEM.getIndex()] - prevTicks[TickType.SYSTEM.getIndex()];
        //用户CPU时间
        long user = ticks[TickType.USER.getIndex()] - prevTicks[TickType.USER.getIndex()];
        // I/O等待时间
        long iowait = ticks[TickType.IOWAIT.getIndex()] - prevTicks[TickType.IOWAIT.getIndex()];
        //空闲时间
        long idle = ticks[TickType.IDLE.getIndex()] - prevTicks[TickType.IDLE.getIndex()];
        //CPU的总使用时间
        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;

        //得到需要的信息，设置到cpu对象中
        cpu.setCpuNum(processor.getLogicalProcessorCount());
        cpu.setTotal(totalCpu);
        cpu.setSys(cSys);
        cpu.setUsed(user);
        cpu.setWait(iowait);
        cpu.setFree(idle);
    }

    //设置内存信息
    private void setMemInfo(GlobalMemory memory) {
        mem.setTotal(memory.getTotal());
        mem.setUsed(memory.getTotal() - memory.getAvailable());
        mem.setFree(memory.getAvailable());
    }

}
