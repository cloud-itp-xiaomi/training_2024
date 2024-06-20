package com.example.springcloud.controller.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ReaderResponse
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-06 18:42
 **/
public class ReaderResponse {
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
