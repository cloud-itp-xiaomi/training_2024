package com.winter.Service;

import com.winter.domain.LogData;
import com.winter.mapper.LogMapper;
import com.winter.req.LogQueryReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 存储和查询日志数据的service层
 * */
@Service
public class LogService {

    @Autowired
    private LogMapper logMapper;

    /**
     * 添加日志数据
     * */
    public void add(LogData logData) {
        logMapper.add(logData);
    }

    /**
     * 查询日志数据
     * */
    public List<LogData> find(LogQueryReq logQueryReq){
        return logMapper.find(logQueryReq);
    }
}
