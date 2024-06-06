package com.winter.resp;


/**
 * 统一返回信息的类
 * */
public class CommonResp<T> {
    private Integer code;  //状态码
    private String message;  //描述,成功则返回ok，失败则返回具体的错误信息
    private T content;  //具体的内容

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

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "CommonResp{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", content=" + content +
                '}';
    }
}
