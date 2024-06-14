package com.example.demo01.config;

import com.example.demo01.bean.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
//用于引入日志工具，以便记录异常信息。
@RestControllerAdvice
//表示这是一个全局异常处理类，用于处理控制器中抛出的异常。
public class GlobalExceptionHandlerResolver {

    @ExceptionHandler(Exception.class)
    //表示捕获所有类型的异常。
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    //表示将异常处理结果设置为HTTP状态码500（内部服务器错误）
    public R handleGlobalException(Exception e) {
        log.error("全局异常信息 ex={}", e.getMessage(), e);
        return R.failed("服务异常");
    }


    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    //表示捕获MethodArgumentNotValidException和BindException类型的异常。
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    //表示将异常处理结果设置为HTTP状态码400（错误请求）
    public R handleBodyValidException(MethodArgumentNotValidException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        log.error("参数绑定异常,ex = {}", fieldErrors.get(0).getDefaultMessage());
        return R.failed(fieldErrors.get(0).getDefaultMessage());
    }

}
