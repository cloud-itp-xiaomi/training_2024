package entity;

import java.util.List;

public class MetricResponseWrapper {
    private int code;
    private String message;
    private List<MetricResponse> data;

    // Getters and setters
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<MetricResponse> getData() {
        return data;
    }

    public void setData(List<MetricResponse> data) {
        this.data = data;
    }
}
