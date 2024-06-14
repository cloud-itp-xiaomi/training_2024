package com.example.demo01.bean;

public class Cpu {
    public static final String METRIC_NAME = "cpu.used.percent";
    //核心数
    private int cpuNum;
    //CPU总的使用率，一般情况下应是一个0到1之间的比例值
    private double total;
    //CPU系统使用率，比例值
    private double sys;
    // CPU用户使用率，比例值
    private double used;
    // CPU当前等待率，比例值
    private double wait;
    //CPU当前空闲率，比例值
    private double free;

    public int getCpuNum() {
        return cpuNum;
    }

    public void setCpuNum(int cpuNum) {
        this.cpuNum = cpuNum;
    }

    //使用率乘以100返回百分比形式
    public double getTotal() {
        return total * 100;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    //先将各自的值除以总使用率(total)得到比例，再乘以100转换为百分比
    public double getSys() {
        return sys / total * 100;
    }

    public void setSys(double sys) {
        this.sys = sys;
    }

    public double getUsed() {
        return used / total * 100;
    }

    public void setUsed(double used) {
        this.used = used;
    }

    public double getWait() {
        return wait / total * 100;
    }

    public void setWait(double wait) {
        this.wait = wait;
    }

    public double getFree() {
        return free / total * 100;
    }

    public void setFree(double free) {
        this.free = free;
    }
}
