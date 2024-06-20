package com.collector.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.collector.bean.entity.CollectorUploadEntity;
import com.collector.bean.request.CollectorQueryRequest;
import com.collector.bean.request.CollectorUploadRequest;
import com.collector.bean.response.CollectorResponse;

import java.util.List;

public interface ICollectorService extends IService<CollectorUploadEntity> {
    Boolean upload(List<CollectorUploadRequest> requests);

    List<CollectorResponse> queryCollectorInfo(CollectorQueryRequest requests);
}
