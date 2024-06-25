package com.example.springcloud.service;

import com.example.springcloud.controller.request.CollectorRequest;
import com.example.springcloud.controller.request.LogUploadRequest;

import java.util.List;

public interface CollectorService {
    void sendCollector(CollectorRequest request);

    CollectorRequest collectCpuMsg();

    CollectorRequest collectMemMsg();

    void logUpload(List<LogUploadRequest> request);
    List<LogUploadRequest> getLogRequest();
}
