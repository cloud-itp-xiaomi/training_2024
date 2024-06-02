package org.qiaojingjing.pojo.vo;

import lombok.Data;

import java.util.List;

@Data
public class MetricVO {
    private String metric;
    private List<Value> values;

    @Data
    public static class Value {
        private Long timestamp;
        private Double value;
    }
}
