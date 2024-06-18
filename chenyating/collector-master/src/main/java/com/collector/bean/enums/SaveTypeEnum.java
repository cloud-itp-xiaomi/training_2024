package com.collector.bean.enums;

public enum SaveTypeEnum {
    LOCAL_FILE(1,"local_file"),
    MYSQL(2,"mysql"),
    ;

    private int code;

    private String message;

    SaveTypeEnum(int code, String message) {
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
