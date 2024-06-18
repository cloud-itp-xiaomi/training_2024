package com.cl.server.handler;

import com.cl.server.entity.Result;
import com.cl.server.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
/**
 * 统一异常捕获处理
 *
 * @author: tressures
 * @date: 2024/6/2
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public Result<String> exceptionHandler(BaseException baseException) {
        log.error("异常原因：{}",baseException.getMessage());
        return Result.error(baseException.getMessage());
    }

}