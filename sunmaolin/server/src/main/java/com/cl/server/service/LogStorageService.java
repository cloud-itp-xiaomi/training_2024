package com.cl.server.service;

import com.cl.server.pojo.DTO.LogInfoDTO;
import com.cl.server.pojo.DTO.LogQueryDTO;
import com.cl.server.pojo.VO.LogInfoVO;
import java.util.List;
/**
 * 日志存储服务接口
 *
 * @author tressures
 * @date:  2024/6/5
 */
public interface LogStorageService {

    void uploadLogs(List<LogInfoDTO> logInfoDTOS);

    LogInfoVO queryLogs(LogQueryDTO logQueryDTO);
}
