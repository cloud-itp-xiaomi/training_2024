package com.h_s.dto;

import java.util.List;

public class ApiResponse {

    private int code;
    private String message;
    private List<Data> data;

    public ApiResponse(int code, String message, List<Data> data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<Data> getData() {
        return data;
    }

    public static ApiResponse success(List<Data> data) {
        return new ApiResponse(200, "ok", data);
    }

    public static ApiResponse error(int code, String message) {
        return new ApiResponse(code, message, null);
    }

    public static class Data {
        private String metric;
        private List<Value> values;

        public Data(String metric, List<Value> values) {
            this.metric = metric;
            this.values = values;
        }

        public String getMetric() {
            return metric;
        }

        public List<Value> getValues() {
            return values;
        }
    }

    public static class Value {
        private long timestamp;
        private double value;

        public Value(long timestamp, double value) {
            this.timestamp = timestamp;
            this.value = value;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public double getValue() {
            return value;
        }
    }
}
