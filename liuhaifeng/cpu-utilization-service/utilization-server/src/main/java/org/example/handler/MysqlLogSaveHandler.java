package org.example.handler;

import org.example.enums.LogSaveTypeEnum;
import org.example.mapper.LogMapper;
import org.example.pojo.entity.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * mysql日志保存处理器
 *
 * @author liuhaifeng
 * @date 2024/06/09/17:39
 */
@Component
public class MysqlLogSaveHandler implements AbstractLogSaveHandler {

    @Autowired
    private LogMapper logMapper;

    @PostConstruct
    public void init() {
        LogSaveHandlerFactory.register(LogSaveTypeEnum.MYSQL, this);
    }

    @Override
    public void upload(List<Log> logsList) {
        logMapper.insertBatch(logsList);
    }

    @Override
    public List<Log> query(Integer endpointId, Integer filePathId, Integer deleted) {
        return logMapper.query(endpointId, filePathId, deleted);
    }
}
