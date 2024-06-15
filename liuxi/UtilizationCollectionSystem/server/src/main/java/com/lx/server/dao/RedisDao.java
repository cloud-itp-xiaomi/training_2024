package com.lx.server.dao;

import com.lx.server.pojo.Utilization;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class RedisDao {

    @Resource(name = "redisTemplate")
    private ValueOperations<String, Object> operations ;
    private static final int MAX_KEYS = 10; //redis中只存储最近的十条数据

    public void addRedis(Utilization utilization) {

        String key = buildStr(utilization.getEndpoint() , utilization.getMetric() , utilization.getTimestamp().toString());//设计存储在redis中的key
        Set<String> keys = operations.getOperations().keys("*");
        if(keys != null && keys.size() == MAX_KEYS){
            delete(keys);
        }
        operations.set(key , utilization);
    }

    //查询相应指标
    public List<Utilization> queryByMetricRedis(String endpoint , String metric , Long start_ts , Long end_ts) {

        Set<String> keys = operations.getOperations().keys("*");
        if(keys == null)
            return null;
        //符合查询条件的key
        List<String> resKey = new ArrayList<>();
        List<Utilization> res = new ArrayList<>();
       for(String key : keys) {
           String[] parts = key.split("_");
           Long timeStamp = Long.parseLong(parts[2]);
           if(parts[0].equals(endpoint) && parts[1].equals(metric) && timeStamp >= start_ts && timeStamp <= end_ts ) {
               resKey.add(key);
           }
       }
       for(String key : resKey) {
           Utilization utilization = (Utilization) operations.get(key);
           res.add(utilization);
       }
       return res;
    }

    //查询全部指标
    public List<Utilization> queryByRedis(String endpoint ,  Long start_ts , Long end_ts) {
        Set<String> keys = operations.getOperations().keys("*");
        if(keys == null)
            return null;
        //符合查询条件的key
        List<String> resKey = new ArrayList<>();
        List<Utilization> res = new ArrayList<>();
        for(String key : keys) {
            String[] parts = key.split("_");
            Long timeStamp = Long.parseLong(parts[2]);
            if(parts[0].equals(endpoint) && timeStamp >= start_ts && timeStamp <= end_ts ){
                resKey.add(key);
            }
        }
        for(String key : resKey) {
            Utilization utilization = (Utilization) operations.get(key);
            res.add(utilization);
        }
        return res;
    }

    //删除redis中最老的key
    public void delete(Set<String> keys) {
        Long old = Long.MAX_VALUE;
        String target = "";
        for(String key : keys) {
            String[] parts = key.split("_");
            Long timeStamp = Long.parseLong(parts[2]);
            if(timeStamp < old) {
                old = timeStamp;
                target = key;
            }
        }
        if(target.length() != 0)
            operations.getAndDelete(target);
    }

    //构建key
    public String buildStr(String... strs) {
        StringBuilder sb = new StringBuilder();
        for(String str : strs) {
            sb.append(str);
            sb.append("_");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }
}
