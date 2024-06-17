package com.example.springcloud.base.Enums;

public enum MetricEnum {
    CPU(1,"cpu.used.percent"),
    MEM(2,"mem.used.percent");
    private final Integer code;
    private final String name;
    MetricEnum(Integer code,String name){
        this.code = code;
        this.name = name;
    }
    public Integer getCode() {
        return code;
    }
    public String getName() {
        return name;
    }
    public static String getName(Integer code) {
        for (MetricEnum metricEnum : MetricEnum.values()) {
            if (metricEnum.getCode().equals(code)) {
                return metricEnum.getName();
            }
        }
        return null;
    }
    public static Integer getCode(String name) {
        for (MetricEnum metricEnum : MetricEnum.values()) {
            if (metricEnum.getName().equals(name)) {
                return metricEnum.getCode();
            }
        }
        return null;
    }
}
