package org.example.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 指标类型枚举类
 *
 * @author liuhaifeng
 * @date 2024/05/30/16:55
 */
@AllArgsConstructor
@Getter
public enum MetricTypeEnum {

    /**
     * cpu利用率
     */
    CPU_USED_PERCENT(1, "cpu.used.percent"),

    /**
     * 内存利用率
     */
    MEM_USED_PERCENT(2, "mem.used.percent");

    private final Integer code;

    private final String value;

    /**
     * 通过code获取枚举类
     *
     * @param code
     * @return
     */
    public static MetricTypeEnum getByCode(Integer code) {
        for (MetricTypeEnum metricTypeEnum : MetricTypeEnum.values()) {
            if (Objects.equals(metricTypeEnum.getCode(), code)) {
                return metricTypeEnum;
            }
        }
        return null;
    }

    /**
     * 通过value获取枚举类
     *
     * @param value
     * @return
     */
    public static MetricTypeEnum getByValue(String value) {
        for (MetricTypeEnum metricTypeEnum : MetricTypeEnum.values()) {
            if (Objects.equals(metricTypeEnum.getValue(), value)) {
                return metricTypeEnum;
            }
        }
        return null;
    }
}
