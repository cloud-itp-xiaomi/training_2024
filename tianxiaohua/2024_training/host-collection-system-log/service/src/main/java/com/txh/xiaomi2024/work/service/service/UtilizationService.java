package com.txh.xiaomi2024.work.service.service;

import com.txh.xiaomi2024.work.service.bean.Utilization;

import java.util.List;

public interface UtilizationService {
    /**
     * 查询
     * @param metric
     * @param endpoint
     * @param startTime
     * @param endTime
     * @return
     */
    List<Utilization> getUtilization(String metric, String endpoint, long startTime, long endTime);
}
