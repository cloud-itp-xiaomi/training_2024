package com.winter.factory;

import com.winter.Service.LogService;
import com.winter.domain.LogData;
import com.winter.req.LogQueryReq;
import com.winter.utils.BeanUtils;

import java.util.List;

/**
 * 采用MYSQL存储数据
 * 直接注入持久层的方案，调用持久层的方法
 * */
public class MySQLStore implements LogStore {

    private LogService logService = BeanUtils.getBean(LogService.class);

    /**
     * MySQL存储数据
     * */
    @Override
    public void storeData(LogData logData) {
        System.out.println("调用mysql存储。。。。");
        logService.add(logData);
    }

    /**
     * MySQL查询数据
     * */
    @Override
    public List<LogData> queryData(LogQueryReq logQueryReq) {
        return logService.find(logQueryReq);
    }

}
