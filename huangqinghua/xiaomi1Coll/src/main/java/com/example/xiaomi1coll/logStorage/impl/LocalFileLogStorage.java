package com.example.xiaomi1coll.logStorage.impl;

import com.example.xiaomi1coll.entity.Logs;
import com.example.xiaomi1coll.logStorage.LogStorageStrategy;
import com.example.xiaomi1coll.tools.HttpTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

public class LocalFileLogStorage implements LogStorageStrategy {
    // 本地存储

    private static final Logger logger = LoggerFactory.getLogger(LocalFileLogStorage.class);
    private static final String Local_LOG_UPLOAD_URL = "http://localhost:8080/api/log/upload/local";

    @Override
    public void storeLog(Logs logs) {
        // 实现本地文件存储逻辑
        try {
            ResponseEntity<String> response = HttpTools.sendPostRequest(Local_LOG_UPLOAD_URL, logs);
            // 处理响应结果
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Logs successfully uploaded to local file storage: {}", logs);
            } else {
                logger.warn("Failed to upload logs to local file storage. Response Status: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Error uploading logs to local file storage: {}", logs, e);
        }
    }
}
