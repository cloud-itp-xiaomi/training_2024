package com.winter.enums;

/**
 * 状态码的枚举
 * */
public enum StatusEnum {
    UPLOAD_SUCCESS(2000,"数据上报成功"),
    UPLOAD_FAIL(2001,"数据上报失败"),
    QUERY_SUCCESS(2002,"请求成功"),
    QUERY_FAIL(2003, "请求失败"),
    SYSTEM_ERROR(2004,"系统错误");

    private Integer code;
    private String desc;

    StatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
