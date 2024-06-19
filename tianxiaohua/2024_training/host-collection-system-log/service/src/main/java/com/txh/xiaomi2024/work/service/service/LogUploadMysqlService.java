package com.txh.xiaomi2024.work.service.service;

import java.util.List;

public interface LogUploadMysqlService {
    String upload(String hostName, String file, List<String> log, long lastUpdateTime);
    long getLastUpdateTime(String hostName, String file);
}
