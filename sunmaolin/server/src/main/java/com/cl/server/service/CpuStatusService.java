package com.cl.server.service;

import com.cl.server.entity.CpuStatus;
import com.cl.server.entity.DTO.StatusQueryDTO;
import com.cl.server.entity.VO.StatusResp;


import java.util.List;

/**
 * (CpuStatus)表服务接口
 *
 * @author tressures
 * @date:  2024-05-26 17:06:02
 */
public interface CpuStatusService {

    void uploadMetrics(List<CpuStatus> cpuStatusList);

    List<StatusResp> queryMetrics(StatusQueryDTO statusQueryDTO);

}
