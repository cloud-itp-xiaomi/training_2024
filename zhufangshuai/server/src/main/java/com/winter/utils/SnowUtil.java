package com.winter.utils;


import cn.hutool.core.util.IdUtil;

/**
 * 雪花算法生成id
 * */
public class SnowUtil {
    //数据中心id, 此处固定写法，后学也可以设计为动态的，具体做法是应用启动时从数据库中查询
    private static long dataCenterId = 1;

    private static long workerId = 1; //机器标识

    public static long getSnowflakeNextId(){
        return IdUtil.getSnowflake(dataCenterId, workerId).nextId();
    }

    public static String getSnowflakeNextIdStr() {
        return IdUtil.getSnowflake(workerId, dataCenterId).nextIdStr();
    }
}
