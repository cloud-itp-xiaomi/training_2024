package com.h_s.service;

import com.h_s.service.Impl.LocalFileLogServiceImpl;
import com.h_s.service.Impl.MysqlLogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LogServiceFactory {

    private final LocalFileLogServiceImpl localFileLogService;
    private final MysqlLogServiceImpl mysqlLogService;

    @Autowired
    public LogServiceFactory(LocalFileLogServiceImpl localFileLogService, MysqlLogServiceImpl mysqlLogService) {
        this.localFileLogService = localFileLogService;
        this.mysqlLogService = mysqlLogService;
    }

    public LogService getLogService(String logStorage) {
        switch (logStorage.toLowerCase()) {
            case "local_file":
                return localFileLogService;
            case "mysql":
                return mysqlLogService;
            default:
                throw new IllegalArgumentException("Unknown log storage type: " + logStorage);
        }
    }
}
