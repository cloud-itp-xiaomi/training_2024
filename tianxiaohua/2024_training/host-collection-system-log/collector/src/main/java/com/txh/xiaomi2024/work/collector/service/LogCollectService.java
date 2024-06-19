package com.txh.xiaomi2024.work.collector.service;

import java.util.List;

/**
 * 获取文件logs
 */
public interface LogCollectService {
    List<String> collectLogs(String filePath);
}
