package com.example.demo.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private int code; // 状态码
    private String message; // 结果描述信息
    private T data; // 返回的结果
    //静态泛型方法：成功(带返回结果)
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode( ResultCode.SUCCESS.getCode() );
        result.setMessage( ResultCode.SUCCESS.getMessage() );
        result.setData(data);
        return result;
    }
    //静态泛型方法：通用失败 (返回结果为null，无需调用setData)
    public static <T> Result<T> fail() {
        Result<T> result = new Result<>();
        result.setCode( ResultCode.FAIL.getCode() );
        result.setMessage( ResultCode.FAIL.getMessage() );
        return result;
    }
    //静态泛型方法：其他在枚举类中的失败 (返回结果为null)
    public static <T> Result<T> fail(ResultCode resultCode) {
        Result<T> result = new Result<>();
        result.setCode( resultCode.getCode() );
        result.setMessage( resultCode.getMessage() );
        return result;
    }
    //静态泛型方法：自定义失败（用于扩充不在枚举类中的失败）
    public static <T> Result<T> fail(int code,String message) {
        return new Result(code,message);
    }
    //增加2个参数的构造函数
    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }
}