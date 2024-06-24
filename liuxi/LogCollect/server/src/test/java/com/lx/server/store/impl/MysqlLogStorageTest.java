package com.lx.server.store.impl;

import com.lx.server.dao.RedisDao;
import com.lx.server.pojo.LogMessage;
import com.lx.server.pojo.LogResult;
import com.lx.server.pojo.ResLog;
import com.lx.server.utils.GetBeanUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DependsOn(value = "getBeanUtil")
class MysqlLogStorageTest {

    @Autowired
    private GetBeanUtil getBeanUtil;

    private MysqlLogStorage mysqlLogStorage;

    @PostConstruct
    public void testGetBeanUtil() {
       mysqlLogStorage = GetBeanUtil.getBean(MysqlLogStorage.class);;
    }

    @Test
    void storeLog() {
        LogMessage logMessage = new LogMessage();
        logMessage.setHostName("123");
        logMessage.setFile("a.log");
        List<String> logs = new ArrayList<>();
        logs.add("this is a new log");
        logMessage.setLogs(logs);
        boolean res = mysqlLogStorage.storeLog(logMessage);
        assert true == res;
    }

    @Test
    void queryLog() {
        LogResult logResult = mysqlLogStorage.queryLog("123", "a.log");
        ResLog resLog = logResult.getData();
        int size = resLog.getLogs().size();
        assert 1 == size;
    }
}