package com.winter.enums;

/**
 * 关于MQ主题的枚举类
 * */
public enum MQTopicEnum {
    LOG_STORAGE("LOG_STORAGE", "日志的存储方式"),
    LOG_DATA("LOG_DATA", "日志数据");

    private String code;
    private String desc;

    MQTopicEnum() {
    }

    MQTopicEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
