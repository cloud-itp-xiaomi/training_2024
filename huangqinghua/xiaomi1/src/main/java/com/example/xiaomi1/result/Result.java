package com.example.xiaomi1.result;

public class Result {
    private Integer code;
    private String message;
    private String data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Result success(){
        Result result=new Result();
        result.setCode(200);
        result.setMessage("");
        result.setData("");
        return result;
    }
}
