package com.jiuth.sysmonitorserver.dao.enity;


import jakarta.persistence.*;

@Entity
@Table(name = "SysInfoCapture")
public class SysInfoCapture {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="metric")
    private String metric;

    @Column(name="endpoint")
    private String endpoint;

    @Column(name="timestamp")
    private long timestamp;

    @Column(name = "step")
    private long step;

    @Column(name="value")
    private double value;

    @Column(name="tags")
    private String tags;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "SysInfoCapture{" +
                "id=" + id +
                ", metric='" + metric + '\'' +
                ", endpoint='" + endpoint + '\'' +
                ", timestamp=" + timestamp +
                ", step='" + step + '\'' +
                ", value='" + value + '\'' +
                ", tags='" + tags + '\'' +
                '}';
    }
}

