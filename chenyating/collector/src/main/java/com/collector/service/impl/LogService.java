package com.collector.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.collector.bean.entity.DetailLogEntity;
import com.collector.bean.entity.LogUploadEntity;
import com.collector.bean.request.LogQueryRequest;
import com.collector.bean.request.LogUploadRequest;
import com.collector.bean.response.LogResponse;
import com.collector.utils.Common;
import com.collector.mapper.LogMapper;
import com.collector.service.ILogService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class LogService extends ServiceImpl<LogMapper, LogUploadEntity> implements ILogService {
    @Resource
    private Common common;
    @Resource
    private LogMapper logMapper;

    @Override
    public LogResponse queryLogInfo(LogQueryRequest request) {
        LogResponse response = new LogResponse();
        LambdaQueryWrapper<LogUploadEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LogUploadEntity::getHostname,request.getHostname())
                .eq(LogUploadEntity::getFile,request.getFile());
        LogUploadEntity logUploadEntity = logMapper.selectOne(wrapper);

        // 根据日志id列表查询日志明细对象，再将符合条件的logs数据转换为数组，并赋值给response对象
        List<Integer> logIds = Collections.singletonList(logUploadEntity.getId());
        List<DetailLogEntity> collectorDetailLogEntities = logMapper.selectDetailLogInfo(logIds);

        // 通过日志id获取日志明细
        logUploadEntity.setLogs(collectorDetailLogEntities.stream()
                .filter(t -> Objects.equals(t.getLogId(), logUploadEntity.getId()))
                .map(DetailLogEntity::getLogs).toArray(String[]::new));
        BeanUtils.copyProperties(logUploadEntity, response);

        return response;
    }

    @Override
    public void upload(List<LogUploadRequest> requests) {
        requests.forEach(request -> {
            // 先判断是否已经存在日志信息，避免添加重复日志信息
            LogQueryRequest logQueryRequest = new LogQueryRequest();
            logQueryRequest.setFile(request.getFile());
            logQueryRequest.setHostname(request.getHostname());
            LogResponse response = queryLogInfo(logQueryRequest);

            if (response != null && response.getId() != null) {
                int logId = response.getId();
                logMapper.deleteDetailLogInfo(logId);
                for (String requestLog : request.getLogs()) {
                    logMapper.saveDetailLogInfo(logId, requestLog, new Date());
                }
            } else {
                LogUploadEntity entity = new LogUploadEntity();
                entity.setFile(request.getFile());
                entity.setHostname(request.getHostname());
                entity.setSystemType(common.getSystemType());
                entity.setCreateTime(new Date());
                int insert = logMapper.insert(entity);

                if(insert > 0){
                    int logId = entity.getId();
                    for (String requestLog : request.getLogs()) {
                        logMapper.saveDetailLogInfo(logId, requestLog, new Date());
                    }
                }
            }
        });
    }
}
