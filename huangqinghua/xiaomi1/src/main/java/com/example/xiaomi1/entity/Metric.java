package com.example.xiaomi1.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;


@TableName(value = "collection")
public class Metric {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String metric;
    private String endpoint;
    private long timestamp;
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

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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
                ", endpoint=' "+endpoint+'\''+
                ", timestamp=' "+timestamp+'\''+
                ", step=' "+step+'\''+
                ", value=' "+value+'\''+
                '}';
    }
}
