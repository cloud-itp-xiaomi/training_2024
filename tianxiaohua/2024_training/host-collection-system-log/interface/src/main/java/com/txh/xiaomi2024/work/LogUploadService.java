package com.txh.xiaomi2024.work;

import java.util.List;

public interface LogUploadService {
    /**
     * 上报日志
     * @param hostname
     * @param file
     * @param logs
     * @param logStorage
     * @param lastUpdateTime
     * @return
     */
    String upload(String hostname, String file, List<String> logs, String logStorage, long lastUpdateTime);

    /**
     * 查询日志的最后更新时间
     * @param hostname
     * @param file
     * @return
     */
    long oldLogFileUpdateTime(String hostname, String file, String logStorage);
}
