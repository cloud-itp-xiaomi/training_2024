package com.cl.collector.service;

import java.io.IOException;

public interface StatusCollectorService {

    Double getCpuUsage() throws IOException;

    Double getMemUsage() throws IOException;
}
