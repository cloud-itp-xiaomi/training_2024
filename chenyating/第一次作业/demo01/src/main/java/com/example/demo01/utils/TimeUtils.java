package com.example.demo01.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
//@UtilityClass 是 Lombok 提供的一个注解，用于标记一个类为实用工具类。
// 被 @UtilityClass 注解标记的类将会被 Lombok 自动添加私有构造函数和静态方法，使得该类可以方便地被其他类使用，而无需创建实例。
public class TimeUtils {
    public static long format(long time) {
        final String timeStr = String.valueOf(time);
        if (timeStr.length() == 13) {//毫秒转秒
            return time / 1000;
        }
        throw new RuntimeException("时间戳格式不正确");
    }
}
