package com.example.mi1.service;


import com.example.mi1.common.utils.PathUtils;

import java.io.IOException;

public interface ESIndexInitService {
    String LOG_JSON = PathUtils.concat("mapping", "log.json");
    void initLOGIndex(String indexName) throws IOException;// 初始化无人机遥感数据在 ES 中的 index
}
