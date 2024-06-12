package com.example.demo01.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataItem implements Serializable {
    private int id;
    private String metric;//指标名称
    private String endpoint;//主机名称
    private Integer timestamp;//采集时间
    private Integer step;//采集周期
    private double value;//采集利用率

    public DataItem(String metric, String endpoint, Integer timestamp, Integer step, double value) {
        this.metric = metric;
        this.endpoint = endpoint;
        this.timestamp = timestamp;
        this.step = step;
        this.value = value;
    }

    public DataItem(String metric, Integer timestamp, double value) {
        this.metric = metric;
        this.timestamp = timestamp;
        this.value = value;
    }

}
