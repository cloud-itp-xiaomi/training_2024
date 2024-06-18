package com.example.springcloud.service;

import com.example.springcloud.controller.request.MetricUploadRequest;
import com.example.springcloud.controller.request.MetricQueryRequest;
import com.example.springcloud.controller.response.MetricQueryResponse;

import java.util.List;

public interface ServerService {
    /**
     * 上报数据
     */
    void metricUpload(MetricUploadRequest request);

    List<MetricQueryResponse> queryMetric(MetricQueryRequest request);
}
