package com.example.xiaomi1coll.logStorage;

import com.example.xiaomi1coll.logStorage.impl.LocalFileLogStorage;
import com.example.xiaomi1coll.logStorage.impl.MySQLLogStorage;

public class LogStorageFactory {
    public static LogStorageStrategy getLogStorageStrategy(String type) {
        return switch (type.toLowerCase()) {
            case "local_file" -> new LocalFileLogStorage();
            case "mysql" -> new MySQLLogStorage();
            default -> throw new IllegalArgumentException("Invalid log storage type: " + type);
        };
    }
}
