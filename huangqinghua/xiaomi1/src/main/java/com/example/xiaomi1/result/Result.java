package com.example.xiaomi1.result;

public class Result {
    private Integer code;
    private String message;
    private Object data;

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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    // 返回成功的结果
    public Result success(Object data){
        Result result=new Result();
        result.setCode(200);
        result.setMessage("ok");
        result.setData(data);
        return result;
    }

    // 返回失败的结果，使用默认的错误码和消息
    public Result failure(){
        return  failure(500, "failure");
    }

    // 返回失败的结果，允许自定义错误码和消息
    public Result failure(Integer errorCode, String errorMessage){
        Result result=new Result();
        result.setCode(errorCode);
        result.setMessage(errorMessage);
        result.setData("");
        return result;
    }
}