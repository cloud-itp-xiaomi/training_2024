package com.hw.server.constants;

/**
 * @author mrk
 * @create 2024-05-22-15:26
 */
public class RedisConstants {

    /**
     * 缓存指标数据前缀
     */
    public static final String CACHE_METRIC_KEY = "cache:metrics";

    /**
     * 缓存指标数据有效期
     */
    public static final Long CACHE_METRIC_TTL = 30L;
}
