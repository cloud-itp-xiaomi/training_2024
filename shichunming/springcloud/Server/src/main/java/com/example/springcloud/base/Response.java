package com.example.springcloud.base;

import com.example.springcloud.base.enums.ErrorCode;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName Response
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-04 20:32
 **/
@Data
public class Response<T> implements Serializable {
    private static final long serialVersionUID = 7028012685967110709L;

    private Integer code;

    private String msg;

    private Boolean success;
    private T data;

    public Response(Boolean success, Integer code, String msg, T data) {
        this.success = success;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Response(ErrorCode errorCode, T data) {
        this.success = errorCode == ErrorCode.SUCCESS;
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
        this.data = data;
    }

    public Response() {
    }

    public static <Void> Response<Void> success() {
        return success(null);
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(ErrorCode.SUCCESS, data);
    }

    public static <T> Response<T> failed() {
        return new Response<>(ErrorCode.SYSTEM_ERROR, null);
    }

    public static <T> Response<T> failed(ErrorCode errorCode) {
        return failed(errorCode.getCode(), errorCode.getMsg());
    }

    public static <T> Response<T> failed(Integer code, String msg) {
        return new Response<>(false, code, msg, null);
    }

    public static <T> Response<T> failed(ErrorCode errorCode, T data) {
        return new Response<>(false, errorCode.getCode(), errorCode.getMsg(), data);
    }

    public boolean isSuccess() {
        return ErrorCode.SUCCESS.getCode() == code;
    }
}
