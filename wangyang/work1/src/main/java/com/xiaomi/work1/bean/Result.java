package com.xiaomi.work1.bean;

import lombok.Data;

/**
 * ClassName: Result
 * Package: com.xiaomi.work1.bean
 * Description:返回结果类
 *
 * @Author WangYang
 * @Create 2024/5/24 21:34
 * @Version 1.0
 */
@Data
public class Result<T> {
    private int code;
    private String message;
    private T data;

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result() {
    }

    public static <T> Result<T> Ok() {
        return new Result<>(200, "ok", null);
    }
    public static <T> Result<T> Ok(T data) {
        return new Result<>(200, "ok", data);
    }
    public static <T> Result<T> error(String msg) {
        return new Result<>(200, msg, null);
    }


}
