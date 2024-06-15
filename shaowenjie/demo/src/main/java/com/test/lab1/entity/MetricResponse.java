package com.test.lab1.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class MetricResponse {
    private String metric;
    private List<ValueData> values;

    @Setter
    @Getter
    public static class ValueData {
        private long timestamp;
        private double value;

    }
}
