package com.cl.server.service;

import com.cl.server.entity.Status;
import com.cl.server.pojo.DTO.StatusQueryDTO;
import com.cl.server.pojo.VO.StatusResp;
import java.util.List;
/**
 * (Status)表服务接口
 *
 * @author tressures
 * @date:  2024-05-26 17:06:02
 */
public interface StatusService {

    void uploadMetrics(List<Status> statusList);

    List<StatusResp> queryMetrics(StatusQueryDTO statusQueryDTO);
}
