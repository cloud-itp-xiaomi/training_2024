package com.collector.bean.enums;

public enum MetricEnum {
    CPU(1,"cpu.used.percent"),
    MEM(2,"mem.used.percent"),
    ;

    private int code;

    private String message;

    MetricEnum(int code, String message) {
        this.code = code;
        this.message =message;
    }

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
