package com.txh.xiaomi2024.work.service.service.impl;

import com.txh.xiaomi2024.work.service.bean.Utilization;
import com.txh.xiaomi2024.work.service.dao.RedisDao;
import com.txh.xiaomi2024.work.service.dao.UtilizationMysqlMapper;
import com.txh.xiaomi2024.work.service.service.UtilizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author txh
 * 查询接口
 */
@Service
public class UtilizationServiceImpl implements UtilizationService {
    private final UtilizationMysqlMapper utilizationMysqlMapper;
    private final RedisDao redisDao;

    @Autowired
    public UtilizationServiceImpl(UtilizationMysqlMapper utilizationMysqlMapper, RedisDao redisDao) {
        this.utilizationMysqlMapper = utilizationMysqlMapper;
        this.redisDao = redisDao;
    }

    @Override
    public List<Utilization> getUtilization(String metric, String endpoint, long startTime, long endTime) {
        List<Utilization> utilizationList = new ArrayList<>();
        // 先去redis缓存查询
        List<Object> list1 = redisDao.lRange("utilization",0, 0);
        List<Object> list2 = redisDao.lRange("utilization",0, redisDao.lSize("utilization"));
        Utilization utilization = (Utilization) list1.get(0);
        // 如果startTime比redis中存储的第一条数据的时间都大，就去redis查找，否则就去mysql
        if (startTime > utilization.getCollect_time()) {
            for (Object o : list2) {
                Utilization u = (Utilization) o;
                if (startTime <= u.getCollect_time()&&endTime >= u.getCollect_time()) {
                    if(u.getMetric().equals(metric)) {
                        utilizationList.add(u);
                    }
                }
            }
        }
        // 再去mysql查询
        else {
            utilizationList = utilizationMysqlMapper.query(endpoint, metric, startTime, endTime);
        }
        return utilizationList;
    }
}
