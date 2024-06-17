package com.xiaomi.server.storage.factory;

import com.xiaomi.server.storage.Impl.LocalFileLogStorage;
import com.xiaomi.server.storage.Impl.MySQLLogStorage;
import com.xiaomi.server.storage.LogStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class LogStorageFactory {
    @Value("${log_storage}")
    private String logStorageType;

    @Bean
    public LogStorage getLogStorage() {
        switch (logStorageType) {
            case "local_file":
                return new LocalFileLogStorage();
            case "mysql":
                return new MySQLLogStorage();
            default:
                throw new IllegalArgumentException("Invalid log storage type");
        }
    }
}
