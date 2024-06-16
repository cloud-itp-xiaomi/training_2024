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
import org.example.pojo.entity.LogConfigEntity;
import org.example.service.LogService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 日志服务类
 *
 * @author liuhaifeng
 * @date 2024/06/05/16:51
 */
@Slf4j
@Service
@RefreshScope
public class LogServiceImpl implements LogService {

    @Value("${utilization-server.config.file-path}")
    private String configFilePath;

    @Value("${utilization-server.log-storage}")
    private String logStorage;

    @Override
    public void upload(List<LogUploadDTO> logUploadDTOList) {
        LogSaveTypeEnum saveType = getLogSaveTypeEnum();
        log.info("日志上传类型为：{}", saveType);
        AbstractLogSaveHandler handler = LogSaveHandlerFactory.getHandler(saveType);
        handler.save(logUploadDTOList);
    }

    @Override
    public LogQueryVO query(LogQueryDTO logQueryDTO) {
        if (logQueryDTO == null) {
            throw new BaseException("日志查询参数不能为空");
        }
        if (StringUtils.isEmpty(logQueryDTO.getHostname())) {
            throw new BaseException("主机名不能为空");
        }
        LogSaveTypeEnum saveType = getLogSaveTypeEnum();
        log.info("日志查询类型为：{}", saveType);
        AbstractLogSaveHandler handler = LogSaveHandlerFactory.getHandler(saveType);
        return handler.query(logQueryDTO);
    }

    private LogSaveTypeEnum getLogSaveTypeEnum() {
        LogConfigEntity logConfigEntity = JSONParseUtil.parseJSONFile(configFilePath);
        if (logConfigEntity == null) {
            throw new BaseException("配置文件解析失败");
        }
        if (!logConfigEntity.getLogStorage().equals(logStorage)) {
            log.info("日志存储方式更改为：{}", logStorage);
            logConfigEntity.setLogStorage(logStorage);
        }
        LogSaveTypeEnum saveType = LogSaveTypeEnum.getByValue(logConfigEntity.getLogStorage());
        if (saveType == null) {
            throw new BaseException("无对应的日志存储类型");
        }
        return saveType;
    }
}
