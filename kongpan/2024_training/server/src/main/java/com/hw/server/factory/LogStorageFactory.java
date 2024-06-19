package com.hw.server.factory;

import com.hw.server.service.ILogsService;
import com.hw.server.service.impl.ElasticsearchLogsServiceImpl;
import com.hw.server.service.impl.MysqlLogsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author mrk
 * @create 2024-06-06-15:49
 */
@Component
@RequiredArgsConstructor
public class LogStorageFactory {

    private final MysqlLogsServiceImpl mysqlLogsService;
    private final ElasticsearchLogsServiceImpl elasticsearchLogsService;

    public ILogsService getLogStorage(String type) {
        return switch (type.toLowerCase()) {
            case "mysql" -> mysqlLogsService;
            case "es" -> elasticsearchLogsService;
            default -> throw new IllegalArgumentException("Unknown log storage type: " + type);
        };
    }
}
