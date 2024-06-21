package com.lx.server.common;

public enum StatusCode {

    FAIL(500, ""),
    PARAM_EMPTY(400, "param is null"),
    PARAM_NOT_VALID(401, "param is not valid"),
    SUCCESS(200, "ok"),
    FILE_NOT_EXIST(404, "file not exists!");

    private int code;
    private String msg;

    StatusCode(int code , String msg){
        this.code = code;
        this.msg = msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

}
