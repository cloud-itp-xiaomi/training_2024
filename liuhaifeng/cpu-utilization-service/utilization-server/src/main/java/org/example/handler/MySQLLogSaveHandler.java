package org.example.handler;

import org.example.enums.LogSaveTypeEnum;
import org.example.exception.BaseException;
import org.example.fegin.pojo.dto.LogQueryDTO;
import org.example.fegin.pojo.dto.LogUploadDTO;
import org.example.fegin.pojo.vo.LogQueryVO;
import org.example.mapper.EndpointMapper;
import org.example.mapper.LogFilePathMapper;
import org.example.mapper.LogMapper;
import org.example.pojo.entity.Endpoint;
import org.example.pojo.entity.Log;
import org.example.pojo.entity.LogFilePath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * mysql日志保存处理器
 *
 * @author liuhaifeng
 * @date 2024/06/09/17:39
 */
@Component
public class MySQLLogSaveHandler implements AbstractLogSaveHandler {

    @Autowired
    private LogMapper logMapper;

    @Autowired
    private EndpointMapper endpointMapper;

    @Autowired
    private LogFilePathMapper logFilePathMapper;

    @PostConstruct
    public void init() {
        LogSaveHandlerFactory.register(LogSaveTypeEnum.MYSQL, this);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(List<LogUploadDTO> logUploadDTOList) {
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
        logMapper.insertBatch(saveLogList);
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
        List<String> logStrList = new ArrayList<>();
        List<Log> query = logMapper.query(endpoint.getId(), logFilePath.getId(), 0);
        for (Log log : query) {
            logStrList.add(log.getLogContent());
        }
        return LogQueryVO
                .builder()
                .hostname(logQueryDTO.getHostname())
                .file(logQueryDTO.getFile())
                .logs(logStrList)
                .build();
    }
}
