package org.example.handler;

import org.example.enums.LogSaveTypeEnum;
import org.example.mapper.LogsMapper;
import org.example.pojo.entity.Logs;
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
    private LogsMapper logsMapper;

    @PostConstruct
    public void init() {
        LogSaveHandlerFactory.register(LogSaveTypeEnum.MYSQL, this);
    }

    @Override
    public void upload(List<Logs> logsList) {
        logsMapper.insertBatch(logsList);
    }

    @Override
    public List<Logs> query(Integer endpointId, String file, Integer deleted) {
        return logsMapper.query(endpointId, file, deleted);
    }
}
