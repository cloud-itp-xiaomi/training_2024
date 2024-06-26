package com.example.hostmonitor.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result {

    private int code;
    private String msg;
    private Object data;

    public static Result success() {
        return new Result(1, "success", null);
    }

    public static Result success(Object data) {
        return new Result(1, "success", data);
    }

    public static Result error(String msg) {
        return new Result(0, msg, null);
    }
}
