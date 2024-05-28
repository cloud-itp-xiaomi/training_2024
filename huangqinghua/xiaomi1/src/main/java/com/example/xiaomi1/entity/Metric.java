package com.example.xiaomi1.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;


@TableName(value = "collection")
public class Metric {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String comName;
    private double cpuUsedPercent;
    private double memUsedPercent;

    private long timeStamp;

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

    public double getCpuUsedPercent() {
        return cpuUsedPercent;
    }

    public void setCpuUsedPercent(double cpuUsedPercent) {
        this.cpuUsedPercent = cpuUsedPercent;
    }

    public double getMemUsedPercent() {
        return memUsedPercent;
    }

    public void setMemUsedPercent(double memUsedPercent) {
        this.memUsedPercent = memUsedPercent;
    }

    @Override
    public String toString() {
        return "Log{" +
                "id=" + id +
                ", comName='" + comName + '\'' +
                ", cpuUsedPercent=' "+cpuUsedPercent+'\''+
                ", memUsedPercent=' "+memUsedPercent+'\''+
                ", timeStamp=' "+timeStamp+'\''+
                '}';
    }
}
