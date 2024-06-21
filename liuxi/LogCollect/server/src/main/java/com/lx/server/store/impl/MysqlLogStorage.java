package com.lx.server.store.impl;

import com.lx.server.common.StatusCode;
import com.lx.server.mapper.LogMapper;
import com.lx.server.pojo.LogMessage;
import com.lx.server.pojo.LogMysql;
import com.lx.server.pojo.LogResult;
import com.lx.server.pojo.ResLog;
import com.lx.server.store.LogStorage;
import com.lx.server.utils.GetBeanUtil;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@DependsOn(value = "getBeanUtil")
public class MysqlLogStorage implements LogStorage {

    private LogMapper logMapper = GetBeanUtil.getBean(LogMapper.class);

    private static final MysqlLogStorage mysqlLogStorage = new MysqlLogStorage();

    private MysqlLogStorage() {
    }

    public static MysqlLogStorage getMysqlLogStorage() {
        return mysqlLogStorage;
    }

    @Override
    public boolean storeLog(LogMessage logMessage) {
        List<String> logs = logMessage.getLogs();
        try {
            for (String log : logs) {
                LogMysql logMysql = new LogMysql();
                logMysql.setHostName(logMessage.getHostName());
                logMysql.setFile(logMessage.getFile());
                logMysql.setLog(log);
                logMapper.insert(logMysql);
            }
            System.out.println("add a logMessage into table log!!!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("add a logMessage failed!!!");
            return false;
        }
        return true;
    }

    @Override
    public LogResult queryLog(String hostName, String filePath) {
        try {
            List<LogMysql> logMysqls = logMapper.query(hostName, filePath);
            ResLog resLog = new ResLog();
            resLog.setHostName(hostName);
            resLog.setFile(filePath);
            List<String> logs = new ArrayList<>();
            for (LogMysql logMysql : logMysqls) {
                logs.add(logMysql.getLog());
            }
            resLog.setLogs(logs);
            return new LogResult(StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.getMsg(), resLog);
        } catch (Exception e) {
            return new LogResult(StatusCode.FAIL.getCode(), StatusCode.FAIL.getMsg(), null);
        }
    }
}
