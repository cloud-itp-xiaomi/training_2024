package com.winter.req;

/**
 * 该类未collector数据上报请求的类，在collector类中也有定义，后续需要将其提取为公共模块
 * */
public class UploadReq {
    private String metric;  //指标名称
    private String endpoint;  //当前主机名称
    private Integer timestamp;  //采集数据的时间
    private Integer step;  //指标的采集周期
    private Double value;  //采集到的具体的值

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

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "UploadReq{" +
                "metric='" + metric + '\'' +
                ", endpoint='" + endpoint + '\'' +
                ", timestamp=" + timestamp +
                ", step=" + step +
                ", value=" + value +
                '}';
    }
}

