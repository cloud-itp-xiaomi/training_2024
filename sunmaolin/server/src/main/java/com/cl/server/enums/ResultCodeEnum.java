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
    FAIL(500,"fail");

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

}
