package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.common.utils.JSONParseUtil;
import org.example.enums.LogSaveTypeEnum;
import org.example.exception.BaseException;
import org.example.fegin.pojo.dto.LogQueryDTO;
import org.example.fegin.pojo.dto.LogUploadDTO;
import org.example.fegin.pojo.vo.LogQueryVO;
import org.example.handler.AbstractLogSaveHandler;
import org.example.handler.LogSaveHandlerFactory;
import org.example.mapper.EndpointMapper;
import org.example.pojo.entity.Endpoint;
import org.example.pojo.entity.LogConfigEntity;
import org.example.pojo.entity.Logs;
import org.example.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 日志服务类
 *
 * @author liuhaifeng
 * @date 2024/06/05/16:51
 */
@Slf4j
@Service
public class LogServiceImpl implements LogService {

    private static final String CONFIG_FILE_PATH = "src/main/java/org/example/config/config.json";

    @Autowired
    private EndpointMapper endpointMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void upload(List<LogUploadDTO> logUploadDTOList) {
        if (CollectionUtils.isEmpty(logUploadDTOList)) {
            throw new BaseException("上传的日志数据为空");
        }
        List<Logs> saveLogsList = new ArrayList<>();
        for (LogUploadDTO logUploadDTO : logUploadDTOList) {
            Endpoint endpoint = endpointMapper.getEndpointByName(logUploadDTO.getHostname());
            Integer endpointId = endpoint != null ? endpoint.getId() : null;
            if (endpointId == null) {
                //新的主机名，插入
                Endpoint saveEndpoint = Endpoint.builder()
                        .name(logUploadDTO.getHostname())
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .deleted(0)
                        .build();
                endpointMapper.insert(saveEndpoint);
                endpointId = saveEndpoint.getId();
            }
            for (String log : logUploadDTO.getLogs()) {
                Logs saveLogs = Logs
                        .builder()
                        .endpointId(endpointId)
                        .file(logUploadDTO.getFile())
                        .log(log)
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .deleted(0)
                        .build();
                saveLogsList.add(saveLogs);
            }
        }
        LogConfigEntity logConfigEntity = JSONParseUtil.parseJSONFile(CONFIG_FILE_PATH);
        if (logConfigEntity == null) {
            throw new BaseException("配置文件解析失败");
        }
        LogSaveTypeEnum saveType = LogSaveTypeEnum.getByValue(logConfigEntity.getLogStorage());
        if (saveType == null) {
            throw new BaseException("无对应的日志存储类型");
        }
        AbstractLogSaveHandler handler = LogSaveHandlerFactory.getHandler(saveType);

        handler.upload(saveLogsList);
    }

    @Override
    public LogQueryVO query(LogQueryDTO logQueryDTO) {
        Endpoint endpoint = endpointMapper.getEndpointByName(logQueryDTO.getHostname());
        if (endpoint == null) {
            throw new BaseException("查询的主机不存在");
        }

        return null;
    }
}
