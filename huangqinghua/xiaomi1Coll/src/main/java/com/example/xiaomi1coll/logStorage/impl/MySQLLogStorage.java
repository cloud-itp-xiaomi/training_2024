package com.example.xiaomi1coll.logStorage.impl;

import com.example.xiaomi1coll.entity.Logs;
import com.example.xiaomi1coll.logStorage.LogStorageStrategy;
import com.example.xiaomi1coll.tools.HttpTools;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySQLLogStorage implements LogStorageStrategy {
    // MySQL存储

    private static final Logger logger = LoggerFactory.getLogger(MySQLLogStorage.class);
    private static final String MYSQL_LOG_UPLOAD_URL = "http://localhost:8080/api/log/upload/MySQL";

    @Override
    public void storeLog(Logs logs) {
        try {
            ResponseEntity<String> response = HttpTools.sendPostRequest(MYSQL_LOG_UPLOAD_URL, logs);
            // 处理响应结果
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Logs successfully uploaded to MySQL: {}", logs);
            } else {
                logger.warn("Failed to upload logs to MySQL. Response Status: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Error uploading logs to MySQL: {}", logs, e);
        }
    }
}
