package com.example.demo01.result;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = {"com.example.demo01.controller"})
public class CustomerExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result<Object> exception(Exception e) {
        String s = e.getMessage(); //获得异常信息内容
        return Result.fail(ResultCode.INTERNAL_SERVER_ERROR.getCode(), s );
    }
}
