package com.txh.xiaomi2024.work.service.service;

import com.txh.xiaomi2024.work.service.bean.Log;
import com.txh.xiaomi2024.work.service.domian.po.LogDocument;

public interface LogQueryService {

    /**
     * 查询log
     * @param hostName
     * @param file
     * @return
     */
    Log getLogFromMysql(String hostName, String file);

    LogDocument getLogFromES(String hostName, String file);
}
