package com.test.lab1.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MetricData {
    private String metric;
    private long timestamp;
    private double value;

    public MetricData(String metric, long timestamp, double value) {
        this.metric=metric;
        this.timestamp=timestamp;
        this.value=value;
    }

    @Override
    public String toString() {
        return "Log{" +
                ", metric='" + metric + '\'' +
                ", timeStamp=' "+timestamp+'\''+
                ", value=' "+value+'\''+
                '}';
    }
}

