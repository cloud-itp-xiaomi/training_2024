package com.hw.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hw.server.domain.Metrics;
import com.hw.server.domain.dto.Result;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mrk
 * @since 2024-05-22
 */
public interface IMetricsService extends IService<Metrics> {

    Result uploadMetrics(List<Metrics> metrics);

    Result queryMetrics(String endpoint, String metric, Long startTs, Long endTs);
}
