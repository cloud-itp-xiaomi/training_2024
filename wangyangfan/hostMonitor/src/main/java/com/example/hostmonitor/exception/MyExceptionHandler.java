package com.example.hostmonitor.exception;

import com.example.hostmonitor.pojo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result ExceptionHandler(Exception e){
        e.printStackTrace();
        return Result.error(e.getMessage());
    }
}
