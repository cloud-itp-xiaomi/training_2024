package com.txh.xiaomi2024.work.service;

import com.txh.xiaomi2024.work.LogUploadService;
import com.txh.xiaomi2024.work.service.service.LogUploadESService;
import com.txh.xiaomi2024.work.service.service.LogUploadMysqlService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import java.util.List;
@DubboService// 通过这个配置可以基于 Spring Boot 去发布 Dubbo 服务
@Service
public class LogUploadServiceImpl implements LogUploadService {
    private final LogUploadMysqlService logUploadMysql;
    private final LogUploadESService logUploadES;

    public LogUploadServiceImpl(LogUploadMysqlService logUploadMysql,
                                LogUploadESService logUploadES) {
        this.logUploadMysql = logUploadMysql;
        this.logUploadES = logUploadES;
    }

    @Override
    public String upload(String hostname,
                         String file,
                         List<String> logs,
                         String logStorage,
                         long lastUpdateTime) {
        switch (logStorage) {
            case "mysql":
                return logUploadMysql.upload(
                        hostname,
                        file,
                        logs,
                        lastUpdateTime) ;
            case "elasticsearch":
                return logUploadES.upload(
                        hostname,
                        file,
                        logs,
                        lastUpdateTime);
            default:
                throw new IllegalArgumentException("Unsupported log storage type: " + logStorage);
        }
    }

    @Override
    public long oldLogFileUpdateTime(String hostname,
                                     String file,
                                     String logStorage) {
        switch (logStorage) {
            case "mysql":
                return logUploadMysql.getLastUpdateTime(
                        hostname,
                        file) ;
            case "elasticsearch":
                return logUploadES.getLastUpdateTime(
                        hostname,
                        file);
            default:
                throw new IllegalArgumentException("Unsupported log storage type: " + logStorage);
        }
    }
}
