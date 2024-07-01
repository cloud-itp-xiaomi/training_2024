package com.winter.resp;


/**
 * value数据类型
 * */
public class Values {
    private Integer timestamp;  //采集的时间
    private Double value;  //采集的具体的值

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Values{" +
                "timestamp=" + timestamp +
                ", value=" + value +
                '}';
    }
}
