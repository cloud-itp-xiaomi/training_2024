package com.txh.xiaomi2024.work.service.service;

import java.util.List;

public interface LogUploadESService {
    String upload(String hostname, String file, List<String> logs, long lastUpdateTime);
    long getLastUpdateTime(String hostname, String file);
}
