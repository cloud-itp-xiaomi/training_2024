package com.test.lab1.result;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Result {
    private Integer code;
    private String message;
    private Object data;

    public Result success(Object data){
        Result result=new Result();
        result.setCode(200);
        result.setMessage("ok");
        result.setData(data);
        return result;
    }

    public Result failure(Integer errorCode, String errorMessage){
        Result result=new Result();
        result.setCode(errorCode);
        result.setMessage(errorMessage);
        result.setData("");
        return result;
    }
}