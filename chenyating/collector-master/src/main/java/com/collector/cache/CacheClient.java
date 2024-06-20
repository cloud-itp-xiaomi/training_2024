package com.collector.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

// redis基本方法
public interface CacheClient {
    void saveLatestData(String key, String value);

    List<Object> getLatestData(String key);

    // 指定缓存失效时间
    boolean expire(String key, long time, TimeUnit timeUnit);

    // 根据key 获取过期时间
    long getExpire(String key,TimeUnit timeUnit);

    // 判断key是否存在
    boolean hasKey(String key);

    @SuppressWarnings("unchecked")
    void del(String ... key);

    // 普通缓存获取
    Object get(String key);

    // 普通缓存获取 ，tClass需要转为的对象
    <T> T get(String key, Class<T> tClass);

    // 普通缓存放入，true成功 false失败
    boolean set(String key,Object value) ;

    // 普通缓存放入并设置时间
    boolean set(String key,Object value,long time);

    // HashGet
    Object hget(String key,String item);

    // 获取hashKey对应的所有键值
    Map<Object,Object> hmget(String key);

    // HashSet
    boolean hmset(String key, Map<String,Object> map);

    boolean hmset(String key, Map<String,Object> map, long time,TimeUnit timeUnit);

    // 向一张hash表中放入数据,如果不存在将创建
    boolean hset(String key,String item,Object value);

    boolean hset(String key,String item,Object value,long time,TimeUnit timeUnit);

    // 删除hash表中的值
    void hdel(String key, Object... item);

    // 判断hash表中是否有该项的值
    boolean hHasKey(String key, String item);

    // hash递增 如果不存在,就会创建一个 并把新增后的值返回，by 要增加几个(大于0)
    double hincr(String key, String item,double by);

    // hash递减，by 要减少几个(小于0)
    double hdecr(String key, String item,double by);
}
