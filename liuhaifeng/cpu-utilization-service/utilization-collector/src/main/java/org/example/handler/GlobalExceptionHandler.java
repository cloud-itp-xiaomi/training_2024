package org.example.handler;

import lombok.extern.slf4j.Slf4j;
import org.example.exception.BaseException;
import org.example.fegin.pojo.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理机
 *
 * @author liuhaifeng
 * @date 2024/05/30/0:08
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public Result<String> exceptionHandler(BaseException baseException) {
        log.error("异常信息：{}", baseException.getMessage());
        return Result.error(baseException.getMessage());
    }
}
