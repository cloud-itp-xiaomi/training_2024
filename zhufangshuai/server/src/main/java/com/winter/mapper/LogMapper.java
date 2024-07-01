package com.winter.mapper;

import com.winter.domain.LogData;
import com.winter.req.LogQueryReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 将日志内容采用MySQL存储
 * */
@Mapper
public interface LogMapper {

    /**
     * 插入日志打数据库
     * */
    void add(@Param("logData") LogData logData);

    /**
     * 查询日志
     * */
    List<LogData> find(@Param("logQueryReq") LogQueryReq logQueryReq);
}
