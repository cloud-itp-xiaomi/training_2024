package org.qiaojingjing.handler;


import lombok.extern.slf4j.Slf4j;
import org.qiaojingjing.exception.BaseException;
import org.qiaojingjing.result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     *
     * @version 0.1.0
     * @author qiaojingjing
     * @since 0.1.0
     **/

    @ExceptionHandler
    public Result exceptionHandler(BaseException ex ){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

}
