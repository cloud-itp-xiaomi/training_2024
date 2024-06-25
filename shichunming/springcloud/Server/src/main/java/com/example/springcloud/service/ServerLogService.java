package com.example.springcloud.service;

import com.example.springcloud.controller.request.LogQueryRequest;
import com.example.springcloud.controller.request.LogUploadRequest;
import com.example.springcloud.controller.request.MetricQueryRequest;
import com.example.springcloud.controller.request.MetricUploadRequest;
import com.example.springcloud.controller.response.LogQueryResponse;
import com.example.springcloud.controller.response.MetricQueryResponse;

import java.util.List;

public interface ServerLogService {
    /**
     * 上报数据
     */
    void logUpload(LogUploadRequest request);

    /**
     * 查询数据
     */
    LogQueryResponse queryLog(LogQueryRequest request);

}
