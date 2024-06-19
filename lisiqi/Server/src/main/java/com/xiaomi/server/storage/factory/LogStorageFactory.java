package com.xiaomi.server.storage.factory;

import com.xiaomi.server.storage.Impl.LocalFileLogStorage;
import com.xiaomi.server.storage.Impl.MySQLLogStorage;
import com.xiaomi.server.storage.LogStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LogStorageFactory {
    @Autowired
    private MySQLLogStorage mySQLLogStorage;

    @Autowired
    private LocalFileLogStorage localFileLogStorage;

    public LogStorage getLogStorage(String storageType) {
        switch (storageType) {
            case "mysql":
                return mySQLLogStorage;
            case "local_file":
                return localFileLogStorage;
            default:
                throw new IllegalArgumentException("Unknown storage type: " + storageType);
        }
    }
}
