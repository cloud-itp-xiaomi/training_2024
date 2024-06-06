package com.winter.domain;

import java.io.Serializable;

/**
 * 机器表现的实体类
 * */
public class Performance implements Serializable {
    private String id;
    private String hostname;
    private Integer timestamp;
    private Integer step;
    private Double cpu_usage;
    private Double mem_usage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
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

    public Double getCpu_usage() {
        return cpu_usage;
    }

    public void setCpu_usage(Double cpu_usage) {
        this.cpu_usage = cpu_usage;
    }

    public Double getMem_usage() {
        return mem_usage;
    }

    public void setMem_usage(Double mem_usage) {
        this.mem_usage = mem_usage;
    }

    @Override
    public String toString() {
        return "Performance{" +
                "id='" + id + '\'' +
                ", hostname='" + hostname + '\'' +
                ", timestamp=" + timestamp +
                ", step=" + step +
                ", cpu_usage=" + cpu_usage +
                ", mem_usage=" + mem_usage +
                '}';
    }
}
