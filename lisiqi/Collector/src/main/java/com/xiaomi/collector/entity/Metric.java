package com.xiaomi.collector.entity;

public class Metric {
    private String metric;
    private String endpoint;
    private long timestamp;
    private long step;
    private double value;

    public Metric() {}

    public Metric(String metric, String endpoint, long timestamp, long step, double value) {
        this.metric = metric;
        this.endpoint = endpoint;
        this.timestamp = timestamp;
        this.step = step;
        this.value = value;
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

    public long getStep() {
        return step;
    }

    public void setStep(long step) {
        this.step = step;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }


}
