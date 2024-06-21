package com.example.springcloud.service;

import com.example.springcloud.controller.request.CollectorRequest;

public interface CollectorService {
    void sendCollector(CollectorRequest request);

    CollectorRequest collectCpuMsg();

    CollectorRequest collectMemMsg();
}
