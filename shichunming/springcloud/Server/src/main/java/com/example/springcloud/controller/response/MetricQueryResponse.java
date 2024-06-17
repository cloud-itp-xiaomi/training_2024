package com.example.springcloud.controller.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName CollectorResponse
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-04 20:53
 **/
@Setter
@Getter
@Data
public class MetricQueryResponse {
    private String metric;
    @Data
    public static class Value {
        private Long timestamp;
        private Float value;

        public Value(Long timestamp, Float value) {
            this.timestamp = timestamp;
            this.value = value;
        }
    }
    private List<Value> values;
    public void setMetric(String metric) {
        this.metric = metric;
    }

    public List<Value> getValues() {
        if (values == null) { // 确保values不是null
            values = new ArrayList<>(); // 初始化values
        }
        return values;
    }
}
