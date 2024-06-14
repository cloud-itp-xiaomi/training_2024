package com.example.demo01.bean;

public class Mem {
    public static final String METRIC_NAME = "mem.used.percent";

    //内存总量，单位字节，后面要转为GB
    private double total;

    // 已用内存，单位字节，后面要转为GB
    private double used;

    //剩余内存，单位字节，后面要转为GB
    private double free;

    public double getTotal() {
        return total / (1024 * 1024 * 1024);
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public double getUsed() {
        return used / (1024 * 1024 * 1024);
    }

    public void setUsed(long used) {
        this.used = used;
    }

    public double getFree() {
        return free / (1024 * 1024 * 1024);
    }

    public void setFree(long free) {
        this.free = free;
    }

    //计算并返回内存使用百分比。
    public double getUsage() {
        return (used / total) * 100;
    }
}
