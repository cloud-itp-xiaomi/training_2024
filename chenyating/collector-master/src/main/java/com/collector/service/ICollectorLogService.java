package com.collector.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.collector.bean.entity.CollectorLogUploadEntity;
import com.collector.bean.request.CollectorLogQueryRequest;
import com.collector.bean.request.CollectorLogUploadRequest;
import com.collector.bean.response.CollectorLogResponse;

import java.util.List;

public interface ICollectorLogService extends IService<CollectorLogUploadEntity> {
    Boolean upload(List<CollectorLogUploadRequest> requests);

    CollectorLogResponse queryLogInfo(CollectorLogQueryRequest request);
}
