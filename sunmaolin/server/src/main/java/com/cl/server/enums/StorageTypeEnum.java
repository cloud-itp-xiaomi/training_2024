package com.cl.server.enums;

import lombok.Getter;

/**
 * 存储方式枚举
 * @author: tressures
 * @date: 2024/6/5
 */
@Getter
public enum StorageTypeEnum {

    LOCAL(1,"local_file"),
    ES(2,"es"),
    MYSQL(3,"mysql");

    public int code;

    public String desc;

    StorageTypeEnum(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public static StorageTypeEnum getByCode(int codeVal){
        for(StorageTypeEnum resultCodeEnum : StorageTypeEnum.values()){
            if(resultCodeEnum.code == codeVal){
                return resultCodeEnum;
            }
        }
        return null;
    }
    public static StorageTypeEnum getByDesc(String type){
        for(StorageTypeEnum resultCodeEnum : StorageTypeEnum.values()){
            if(resultCodeEnum.desc.equals(type)){
                return resultCodeEnum;
            }
        }
        return null;
    }
}
