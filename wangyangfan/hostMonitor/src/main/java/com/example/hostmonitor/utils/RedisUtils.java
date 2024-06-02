package com.example.hostmonitor.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Author: WangYF
 * @Date: 2024/06/01
 * @Description: 处理redis的静态方法
 */
@Component
public class RedisUtils {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * @Description: 设置业务要求的redis保存前n条数据
     */
    public <K, V> void setToHostResourceList(K key, V value, long n){
        redisTemplate.expire(key, 1, TimeUnit.DAYS);
        long difLen = listSize(key) - n + 1;
        while(difLen-- > 0){
            listLeftPop(key);
        }
        listRightPush(key, value);
    }

    /**
     * @Description: 生成键（项目名：表名）
     */
    public String generateKeyName(String metric){
        return "HM:" + metric + ":ID:001";
    }

//    /**
//     * @Description: 设置键值对
//     */
//    public <K, V> void set(K key, V value){
//        try{
//            redisTemplate.opsForValue().set(key, value);
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * @Description: 设置键值对，带过期时间
//     */
//    public <K, V> void set(K key, V value, long time){
//        try{
//            if(time > 0){
//                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
//            } else {
//                redisTemplate.opsForValue().set(key, value);
//            }
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * @Description: 由键获取值
//     */
//    public <K, V> V get(K key){
//        BoundValueOperations<K, V> boundValueOperations = redisTemplate.boundValueOps(key);
//        return boundValueOperations.get();
//    }

    /**
     * @Description: 从列表右边插入
     */
    public <K, V> void listRightPush(K key, V value){
        ListOperations<K, V> listOperations = redisTemplate.opsForList();
        listOperations.rightPush(key, value);
    }

    /**
     * @Description: 从列表左边弹出
     */
    public <K, V> V listLeftPop(K key){
        ListOperations<K, V> listOperations = redisTemplate.opsForList();
        return listOperations.leftPop(key);
    }

    /**
     * @Description: 查看列表长度
     */
    public <K> long listSize(K key){
        Long size = redisTemplate.opsForList().size(key);
        return Objects.requireNonNullElse(size, 0).longValue();
    }

}
