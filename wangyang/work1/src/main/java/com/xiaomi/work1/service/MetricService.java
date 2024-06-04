package com.xiaomi.work1.service;

import com.xiaomi.work1.bean.Metric;
import com.xiaomi.work1.bean.Result;


import java.util.List;

/**
 * ClassName: MetricService
 * Package: com.xiaomi.work1.service
 * Description:实现类接口
 *
 * @Author WangYang
 * @Create 2024/5/24 22:04
 * @Version 1.0
 */
public interface MetricService {
    /**
     * 上报信息
     * @param metrics
     * @return
     */
    Result<?> uploadMetrics(List<Metric> metrics);

    /**
     * 查询信息
     * @param endpoint
     * @param metric
     * @param startTs
     * @param endTs
     * @return
     */
    Result<?> queryMetrics(String endpoint, String metric, Long startTs, Long endTs);
}
