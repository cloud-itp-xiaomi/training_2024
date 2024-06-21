package com.example.springcloud.base;

import com.example.springcloud.base.enums.ErrorCode;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;


/**
 * @ClassName Exception
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-04 22:07
 **/
@Getter
public class MyException extends RuntimeException{
    private final Integer code;
    private final String msg;
    public MyException(ErrorCode errorCode)
    {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
    }
    public MyException(ErrorCode errorCode, String msg)
    {
        super(msg);
        this.code = errorCode.getCode();
        this.msg = msg;
    }
    public MyException(String msg){
        super(msg);
        this.code = ErrorCode.SYSTEM_ERROR.getCode();
        this.msg = StringUtils.isEmpty(msg)? ErrorCode.SYSTEM_ERROR.getMsg():msg;
    }
}
