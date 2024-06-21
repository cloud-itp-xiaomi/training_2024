package com.collector.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.collector.bean.entity.LogUploadEntity;
import com.collector.bean.request.LogQueryRequest;
import com.collector.bean.request.LogUploadRequest;
import com.collector.bean.response.LogResponse;

import java.util.List;

public interface ILogService extends IService<LogUploadEntity> {
    void upload(List<LogUploadRequest> requests);

    LogResponse queryLogInfo(LogQueryRequest request);
}
