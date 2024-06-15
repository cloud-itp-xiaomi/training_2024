package com.hw.server.domain.dto;

import lombok.Data;

/**
 * @author mrk
 * @create 2024-05-22-15:04
 */
@Data
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    public static Result<Void> ok() {
        return ok(null);
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(200, "ok", data);
    }

    public static <T> Result<T> error(String msg) {
        return new Result<>(500, msg, null);
    }

    public static <T> Result<T> error(int code, String msg) {
        return new Result<>(code, msg, null);
    }

    public Result() {
    }

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public boolean success(){
        return code == 200;
    }
}
