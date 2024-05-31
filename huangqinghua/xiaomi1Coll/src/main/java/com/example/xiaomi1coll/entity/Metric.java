package com.example.xiaomi1coll.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;


@TableName(value = "collection")
public class Metric {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String metric;
    private String endPoint;
    private long timeStamp;
    private Integer step;
    private double value;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Log{" +
                "id=" + id + '\'' +
                ", metric='" + metric + '\'' +
                ", endPoint=' "+endPoint+'\''+
                ", timeStamp=' "+timeStamp+'\''+
                ", step=' "+step+'\''+
                ", value=' "+value+'\''+
                '}';
    }
}
