package com.winter.factory;

import com.winter.domain.LogData;
import com.winter.req.LogQueryReq;

import java.util.List;

/**
 * 所有存储方式的父接口
 * */
public interface LogStore {

    /**
     * 存储日志数据
     */
    void storeData(LogData logData);

    /**
     * 查询日志数据
     * */
    List<LogData> queryData(LogQueryReq logQueryReq);
}
