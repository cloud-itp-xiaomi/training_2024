package com.example.springcloud.base.enums;

import java.util.Objects;

public enum MetricEnum {

    //保留
    CPU("cpu.used.percent", "CPU"),
    MEM("mem.used.percent", "Mem");

    private final String code;

    private final String desc;

    MetricEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static boolean check(String code) {
        MetricEnum[] values = values();
        for (MetricEnum value : values) {
            if (Objects.equals(value.code, code)) {
                return false;
            }
        }
        return true;
    }

    public static String getByCode(String code) {
        MetricEnum[] values = values();
        for (MetricEnum value : values) {
            if (Objects.equals(value.code, code)) {
                return value.code;
            }
        }
        return null;
    }
}
