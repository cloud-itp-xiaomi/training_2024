package com.txh.xiaomi2024.work.service.service;

import com.txh.xiaomi2024.work.service.util.PathUtil;

import java.io.IOException;

public interface ESIndexInitService {
    // Data
    String LOG_JSON = PathUtil.concat("mapper", "LogMapping.json");

    void initLogDataIndex(String indexName) throws IOException;// 初始化数据集在 ES 中的 index
}
