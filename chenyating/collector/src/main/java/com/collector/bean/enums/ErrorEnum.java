package com.collector.bean.enums;

public enum ErrorEnum {
    SUCCESS(200, "ok"),
    FAIL(-1, "失败"),
    NOT_FOUND(404, "资源不存在"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错"),
    // 自定义的状态码(码值是自拟)
    ID_NOT_FOUND(4001,"id不存在"),
    ID_DUPLICATED(4002,"id重复"),
    ;

    private long code;

    private String message;

    ErrorEnum(long code, String message) {
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
