package org.example.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 日志保存类型枚举
 *
 * @author liuhaifeng
 * @date 2024/06/09/17:03
 */
@AllArgsConstructor
@Getter
public enum LogSaveTypeEnum {
    /**
     * mysql数据库
     */
    MYSQL(1, "mysql"),
    /**
     * 本地文件存储
     */
    LOCAL_FILE(2, "local_file");

    private final Integer code;

    private final String value;

    /**
     * 通过code获取枚举类
     *
     * @param code
     * @return
     */
    public static LogSaveTypeEnum getByCode(Integer code) {
        for (LogSaveTypeEnum logSaveTypeEnum : LogSaveTypeEnum.values()) {
            if (Objects.equals(logSaveTypeEnum.getCode(), code)) {
                return logSaveTypeEnum;
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
    public static LogSaveTypeEnum getByValue(String value) {
        for (LogSaveTypeEnum logSaveTypeEnum : LogSaveTypeEnum.values()) {
            if (Objects.equals(logSaveTypeEnum.getValue(), value)) {
                return logSaveTypeEnum;
            }
        }
        return null;
    }
}
