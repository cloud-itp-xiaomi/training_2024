package org.qiaojingjing.entity;

import java.util.Map;

public class Metric {
    private String metric; //指标名称,CPU或者内存使用率
    private String endpoint; //当前主机名称
    private Long timestamp; //采集数据的时间
    private Long step; //指标的采集周期(固定为60)
    private Double value; //采集到的CPU 或内存利用率的值
    private Map<String,String> tags;

    public Metric() {
    }

    public Metric(String metric,
                  String endpoint,
                  Long timestamp,
                  Long step,
                  Double value,
                  Map<String, String> tags) {
        this.metric = metric;
        this.endpoint = endpoint;
        this.timestamp = timestamp;
        this.step = step;
        this.value = value;
        this.tags = tags;
    }

    /**
     * 获取
     * @return metric
     */
    public String getMetric() {
        return metric;
    }

    /**
     * 设置
     */
    public void setMetric(String metric) {
        this.metric = metric;
    }

    /**
     * 获取
     * @return endpoint
     */
    public String getEndpoint() {
        return endpoint;
    }

    /**
     * 设置
     */
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * 获取
     * @return timestamp
     */
    public Long getTimestamp() {
        return timestamp;
    }

    /**
     * 设置
     */
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * 获取
     * @return step
     */
    public Long getStep() {
        return step;
    }

    /**
     * 设置
     */
    public void setStep(Long step) {
        this.step = step;
    }

    /**
     * 获取
     * @return value
     */
    public Double getValue() {
        return value;
    }

    /**
     * 设置
     */
    public void setValue(Double value) {
        this.value = value;
    }

    /**
     * 获取
     * @return tags
     */
    public Map<String, String> getTags() {
        return tags;
    }

    /**
     * 设置
     */
    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    public String toString() {
        return "Metric{metric = " + metric + ", endpoint = " + endpoint + ", timestamp = " + timestamp + ", step = " + step + ", value = " + value + ", tags = " + tags + "}";
    }
}
