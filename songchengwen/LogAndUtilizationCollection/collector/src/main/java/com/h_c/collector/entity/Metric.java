package com.h_c.collector.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Metric {
    // 指标名称
    private String metric;
    // 当前主机名称
    private String endpoint;
    // 采集数据时的时间
    private long timestamp;
    // 指标的采集周期
    private int step;
    // CPU 或内存利用率的值
    private double value;
}
