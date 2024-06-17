package org.qiaojingjing.server.result;

import lombok.Data;
/**
 * 统一响应结果
 * @version 0.1.0
 * @author qiaojingjing
 * @since 0.1.0
 * create
 **/
@Data
public class Result {
    private Integer code; //状态码
    private String message; //请求成功:ok 失败：具体信息
    private Object data;

    public static Result success(){
        Result result = new Result();
        result.code = 1;
        result.message = "ok";
        result.data = null;
        return result;
    }

    public static Result success(Object data){
        Result result = new Result();
        result.code = 1;
        result.message = "ok";
        result.data = data;
        return result;
    }

    public static Result error(String message) {
        Result result = new Result();
        result.code = 1;
        result.message = message;
        result.data = null;
        return result;
    }
}
