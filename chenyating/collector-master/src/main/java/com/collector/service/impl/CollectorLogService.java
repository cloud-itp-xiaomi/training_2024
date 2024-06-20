package com.collector.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.collector.bean.entity.CollectorDetailLogEntity;
import com.collector.bean.entity.CollectorLogUploadEntity;
import com.collector.bean.request.CollectorLogQueryRequest;
import com.collector.bean.request.CollectorLogUploadRequest;
import com.collector.bean.response.CollectorLogResponse;
import com.collector.utils.Common;
import com.collector.mapper.CollectorLogMapper;
import com.collector.service.ICollectorLogService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class CollectorLogService extends ServiceImpl<CollectorLogMapper, CollectorLogUploadEntity> implements ICollectorLogService {
    @Resource
    private Common common;
    @Resource
    private CollectorLogMapper collectorLogMapper;

    @Override
    public CollectorLogResponse queryLogInfo(CollectorLogQueryRequest request) {
        CollectorLogResponse response = new CollectorLogResponse();
        LambdaQueryWrapper<CollectorLogUploadEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CollectorLogUploadEntity::getHostname,request.getHostname())
                .eq(CollectorLogUploadEntity::getFile,request.getFile());
        CollectorLogUploadEntity collectorLogUploadEntity = collectorLogMapper.selectOne(wrapper);

        if (collectorLogUploadEntity != null) {
            // 根据日志id列表查询日志明细对象，再将符合条件的logs数据转换为数组，并赋值给response对象
            List<Integer> logIds = Collections.singletonList(collectorLogUploadEntity.getId());
            List<CollectorDetailLogEntity> collectorDetailLogEntities = collectorLogMapper.selectDetailLogInfo(logIds);

            // 通过日志id获取日志明细
            collectorLogUploadEntity.setLogs(collectorDetailLogEntities.stream()
                    .filter(t -> Objects.equals(t.getLogId(), collectorLogUploadEntity.getId()))
                    .map(CollectorDetailLogEntity::getLogs).toArray(String[]::new));
            BeanUtils.copyProperties(collectorLogUploadEntity,response);
        }else{
            throw new RuntimeException("日志信息不存在！");
        }
        return response;
    }

    @Override
    public Boolean upload(List<CollectorLogUploadRequest> requests) {
        requests.forEach(request -> {
            // 先判断是否已经存在日志信息，避免添加重复日志信息
            CollectorLogQueryRequest collectorLogQueryRequest = new CollectorLogQueryRequest();
            collectorLogQueryRequest.setFile(request.getFile());
            collectorLogQueryRequest.setHostname(request.getHostname());
            CollectorLogResponse response = queryLogInfo(collectorLogQueryRequest);

            if (response != null && response.getId() != null) {
                int logId = response.getId();
                collectorLogMapper.deleteDetailLogInfo(logId);
                for (String requestLog : request.getLogs()) {
                    collectorLogMapper.saveDetailLogInfo(logId, requestLog, new Date());
                }
            } else {
                CollectorLogUploadEntity entity = new CollectorLogUploadEntity();
                entity.setFile(request.getFile());
                entity.setHostname(request.getHostname());
                entity.setSystemType(common.getSystemType());
                entity.setCreateTime(new Date());
                int insert = collectorLogMapper.insert(entity);

                if(insert > 0){
                    int logId = entity.getId();
                    for (String requestLog : request.getLogs()) {
                        collectorLogMapper.saveDetailLogInfo(logId, requestLog, new Date());
                    }
                }
            }
        });
        return Boolean.TRUE;
    }
}
