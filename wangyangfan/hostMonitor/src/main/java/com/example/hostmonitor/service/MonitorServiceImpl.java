package com.example.hostmonitor.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.hostmonitor.mapper.HostResourceUsageMapper;
import com.example.hostmonitor.pojo.*;
import com.example.hostmonitor.utils.DataTransUtils;
import com.example.hostmonitor.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: WangYF
 * @Date: 2024/05/31
 * @Description: 服务实现类
 */

@Service
public class MonitorServiceImpl implements MonitorService{

    @Autowired
    private HostResourceUsageMapper hostResourceUsageMapper;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * @Description: 事务保存上传数据
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveAllData(UploadData memData, UploadData cpuData) {
        HostResourceUsageEntity cpuEntity = DataTransUtils.dataConvertToEntity(cpuData);
        HostResourceUsageEntity memEntity = DataTransUtils.dataConvertToEntity(memData);
        hostResourceUsageMapper.insert(cpuEntity);
        hostResourceUsageMapper.insert(memEntity);
        String cpuKey = redisUtils.generateKeyName(cpuEntity.getMetric());
        String memKey = redisUtils.generateKeyName(memEntity.getMetric());
        redisUtils.setToHostResourceList(cpuKey, cpuEntity, 10);
        redisUtils.setToHostResourceList(memKey, memEntity, 10);
    }

    /**
     * @Description: 读取数据
     */
    @Override
    public List<QueryData> queryBetweenTime(QueryMsg queryMsg) {
        String endPoint = queryMsg.getEndpoint();
        String metric = queryMsg.getMetric();
        Long start_ts = queryMsg.getStart_ts();
        Long end_ts = queryMsg.getEnd_ts();

        List<QueryData> res = new ArrayList<>();

        if(null != metric){
            res.add(new QueryData(metric, hostResourceUsageMapper.selectByTimestamp(endPoint, metric, start_ts, end_ts)));
        }
        else {
            QueryWrapper<HostResourceUsageEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("endpoint", endPoint).between("timestamp", start_ts, end_ts);
            List<HostResourceUsageEntity> unOrderData = hostResourceUsageMapper.selectList(queryWrapper);
            res.addAll(DataTransUtils.convertToQueryData(unOrderData));
        }
        return res;
    }
}
