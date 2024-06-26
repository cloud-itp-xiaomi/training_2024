package com.collector.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.collector.bean.entity.MetircUploadEntity;
import com.collector.bean.request.MetircQueryRequest;
import com.collector.bean.request.MetircUploadRequest;
import com.collector.bean.response.MetricResponse;

import java.util.List;

public interface IMetricService extends IService<MetircUploadEntity> {
    void upload(List<MetircUploadRequest> requests);

    List<MetricResponse> queryMetricInfo(MetircQueryRequest requests);
}
