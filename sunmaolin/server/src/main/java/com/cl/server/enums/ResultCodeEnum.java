package com.cl.server.enums;

import lombok.Getter;

/**
 * code枚举
 * 
 * @author: tressures
 * @date: 2024-05-26 17:06:02
 */
@Getter
public enum ResultCodeEnum {

    SUCCESS(200,"ok"),
    PARAM_EMPTY(400,"参数为空"),
    PARAM_BODY_EMPTY(401,"获取指标为空"),
    HOST_NOT_EXIST(403, "主机不存在"),
    INTERNAL_SERVER_ERROR(500,"服务器内部错误");

    public int code;

    public String desc;

    ResultCodeEnum(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public static ResultCodeEnum getByCode(int codeVal){
        for(ResultCodeEnum resultCodeEnum : ResultCodeEnum.values()){
            if(resultCodeEnum.code == codeVal){
                return resultCodeEnum;
            }
        }
        return null;
    }
    public static ResultCodeEnum getByDesc(String desc){
        for(ResultCodeEnum resultCodeEnum : ResultCodeEnum.values()){
            if(resultCodeEnum.desc == desc){
                return resultCodeEnum;
            }
        }
        return null;
    }
}
