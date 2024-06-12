package com.example.hostcollector.exceptions;

import com.example.hostcollector.pojo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public Result RuntimeExceptionHandler(RuntimeException e){
        e.printStackTrace();
        return Result.error(e.getMessage());
    }


    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result ExceptionHandler(Exception e){
        e.printStackTrace();
        return Result.error(e.getMessage());
    }
}
