package com.cl.server.entity;

import com.cl.server.enums.ResultCodeEnum;
import lombok.Data;
/**
 * result统一
 * 
 * @author: tressures
 * @date: 2024-05-26 17:06:02
 */
@Data
public class Result<T> {


    private Integer code;

    private String message;

    private T data;

    public static Result ok() {
        Result result = new Result();
        result.setCode(ResultCodeEnum.SUCCESS.getCode());
        result.setMessage(ResultCodeEnum.SUCCESS.getDesc());
        return result;
    }

    public static <T> Result ok(T data) {
        Result result = new Result();
        result.setCode(ResultCodeEnum.SUCCESS.getCode());
        result.setMessage(ResultCodeEnum.SUCCESS.getDesc());
        result.setData(data);
        return result;
    }


    public static Result error(String message) {
        Result result = new Result();
        result.setCode(ResultCodeEnum.getByDesc(message).code);
        result.setMessage(message);
        return result;
    }

}
