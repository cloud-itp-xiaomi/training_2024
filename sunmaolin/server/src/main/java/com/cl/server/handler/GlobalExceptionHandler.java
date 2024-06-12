package com.cl.server.handler;

import com.cl.server.entity.Result;
import com.cl.server.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public Result<String> exceptionHandler(BaseException baseException) {
        log.error("异常信息：{}",baseException.getMessage());
        return Result.error(baseException.getMessage());
    }

}