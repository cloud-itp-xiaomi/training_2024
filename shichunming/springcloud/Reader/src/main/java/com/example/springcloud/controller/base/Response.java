package com.example.springcloud.controller.base;

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


}
