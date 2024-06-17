package com.jiuth.sysmonitorserver.exception;

import com.jiuth.sysmonitorserver.util.ApiResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理
 * @author jiuth
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ApiResponse<Void> handleException(Exception e) {
        return ApiResponse.error(e.getMessage());
    }
}