package com.cl.server.service.impl;

import com.cl.server.config.LogStorageConfig;
import com.cl.server.handler.storage.StorageTypeHandler;
import com.cl.server.pojo.DTO.LogInfoDTO;
import com.cl.server.pojo.DTO.LogQueryDTO;
import com.cl.server.pojo.VO.LogInfoVO;
import com.cl.server.handler.storage.StorageTypeHandlerFactory;
import com.cl.server.service.LogStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
/**
 * 日志存储服务实现类
 *
 * @author tressures
 * @date:  2024/6/5
 */
@Slf4j
@Service("logStorageService")
public class LogStorageServiceImpl implements LogStorageService {

    @Resource
    private StorageTypeHandlerFactory storageTypeHandlerFactory;

    @Resource
    private LogStorageConfig logStorageConfig;

    @Override
    public void uploadLogs(List<LogInfoDTO> logInfoDTOS) {
        StorageTypeHandler handler = storageTypeHandlerFactory.getHandler(logStorageConfig.getLogStorage());
        handler.upload(logInfoDTOS);
    }

    @Override
    public LogInfoVO queryLogs(LogQueryDTO logQueryDTO) {
        StorageTypeHandler handler = storageTypeHandlerFactory.getHandler(logStorageConfig.getLogStorage());
        return handler.query(logQueryDTO);
    }
}
