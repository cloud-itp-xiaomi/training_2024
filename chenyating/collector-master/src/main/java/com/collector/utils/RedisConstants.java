package com.collector.utils;

public class RedisConstants {
    private RedisConstants() {}

    // Key的前缀
    public static final String REDIS_KEY_PREFIX = "COLLECTOR:KEY";

    // 限制条数
    public static final Integer MAX_VALUE_COUNT = 10;
}
