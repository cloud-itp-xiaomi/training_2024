package com.cl.collector.service;

import java.io.IOException;
/**
 * 采集状态接口
 *
 * @author: tressures
 * @date: 2024/5/26
 */
public interface StatusService {

    Double getCpuUsage() throws IOException;

    Double getMemUsage() throws IOException;
}
