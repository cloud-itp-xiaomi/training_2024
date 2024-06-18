package com.collector.utils;

import com.collector.bean.enums.ErrorEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyResult<T> implements Serializable {
    private static final long serialVersionUID = 1030670245854706374L;

    private long code;
    private String message;
    private T data;

    public static <T> MyResult<T> success(T data) {
        return new MyResult<T>(ErrorEnum.SUCCESS.getCode(),
                ErrorEnum.SUCCESS.getMessage(), data);
    }

    public static <T> MyResult<T> failed(String message) {
        return new MyResult<T>(ErrorEnum.FAIL.getCode(), message, null);
    }

    @Override
    public String toString() {
        return "{" +
                "code=" + code + ",\n" +
                "message=" + message + ",\n" +
                "data=" + data +
                "}";
    }
}
