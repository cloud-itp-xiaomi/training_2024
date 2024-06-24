package com.example.xiaomi1coll.logStorage;

import com.example.xiaomi1coll.entity.Logs;

public interface LogStorageStrategy {
    void storeLog(Logs logs);
}
