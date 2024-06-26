package com.collector.cache;

import java.util.List;

public interface CacheClient {
    void saveLatestData(String key, String value);

    List<Object> getLatestData(String key);
}
