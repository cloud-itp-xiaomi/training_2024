package com.cl.server.service;

import com.cl.server.entity.CpuStatus;
import com.cl.server.entity.CpuStatusResp;

import java.util.List;

/**
 * (CpuStatus)表服务接口
 *
 * @author tressures
 * @date:  2024-05-26 17:06:02
 */
public interface CpuStatusService {

    void uploadMetrics(List<CpuStatus> cpuStatusList);

    List<CpuStatusResp> queryMetrics(String endpoint, String metric, Long start_ts, Long end_ts);

}
