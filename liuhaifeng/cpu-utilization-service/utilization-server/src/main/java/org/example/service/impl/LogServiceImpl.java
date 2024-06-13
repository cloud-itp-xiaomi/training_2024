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
import org.example.mapper.LogFilePathMapper;
import org.example.pojo.entity.Endpoint;
import org.example.pojo.entity.Log;
import org.example.pojo.entity.LogConfigEntity;
import org.example.pojo.entity.LogFilePath;
import org.example.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${utilization-server.config.file.path}")
    private String path;

    @Autowired
    private EndpointMapper endpointMapper;

    @Autowired
    private LogFilePathMapper logFilePathMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void upload(List<LogUploadDTO> logUploadDTOList) {
        if (CollectionUtils.isEmpty(logUploadDTOList)) {
            throw new BaseException("上传的日志数据为空");
        }
        List<Log> saveLogList = new ArrayList<>();
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
            LogFilePath logFilePath = logFilePathMapper.getLogFilePathByFilePath(logUploadDTO.getFile());
            Integer filePathId = logFilePath != null ? logFilePath.getId() : null;
            if (filePathId == null) {
                //新的日志文件路径，插入
                LogFilePath saveLogFilePath = LogFilePath.builder()
                        .filePath(logUploadDTO.getFile())
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .deleted(0)
                        .build();
                logFilePathMapper.insert(saveLogFilePath);
                filePathId = saveLogFilePath.getId();
            }
            for (String log : logUploadDTO.getLogs()) {
                Log saveLogs = Log
                        .builder()
                        .endpointId(endpointId)
                        .filePathId(filePathId)
                        .logContent(log)
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .deleted(0)
                        .build();
                saveLogList.add(saveLogs);
            }
        }
        LogSaveTypeEnum saveType = getLogSaveTypeEnum();
        AbstractLogSaveHandler handler = LogSaveHandlerFactory.getHandler(saveType);
        handler.upload(saveLogList);
    }

    @Override
    public LogQueryVO query(LogQueryDTO logQueryDTO) {
        Endpoint endpoint = endpointMapper.getEndpointByName(logQueryDTO.getHostname());
        if (endpoint == null) {
            throw new BaseException("查询的主机不存在");
        }
        LogFilePath logFilePath = logFilePathMapper.getLogFilePathByFilePath(logQueryDTO.getFile());
        if (logFilePath == null) {
            throw new BaseException("查询的日志文件不存在");
        }
        LogSaveTypeEnum saveType = getLogSaveTypeEnum();
        AbstractLogSaveHandler handler = LogSaveHandlerFactory.getHandler(saveType);
        List<Log> query = handler.query(endpoint.getId(), logFilePath.getId(), 0);
        List<String> logsStrList = new ArrayList<>();
        for (Log log : query) {
            logsStrList.add(log.getLogContent());
        }
        LogQueryVO logQueryVO = LogQueryVO
                .builder()
                .hostname(logQueryDTO.getHostname())
                .file(logQueryDTO.getFile())
                .logs(logsStrList)
                .build();
        return logQueryVO;
    }

    private LogSaveTypeEnum getLogSaveTypeEnum() {
        LogConfigEntity logConfigEntity = JSONParseUtil.parseJSONFile(path);
        if (logConfigEntity == null) {
            throw new BaseException("配置文件解析失败");
        }
        LogSaveTypeEnum saveType = LogSaveTypeEnum.getByValue(logConfigEntity.getLogStorage());
        if (saveType == null) {
            throw new BaseException("无对应的日志存储类型");
        }
        return saveType;
    }
}
