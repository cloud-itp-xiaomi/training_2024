package com.txh.xiaomi2024.work.service.dao;

import com.txh.xiaomi2024.work.service.bean.Log;
import org.apache.ibatis.annotations.Mapper;


/**
 * 数据库操作映射
 * 与.xml文件相呼应
 */
@Mapper
public interface LogMysqlMapper {
    void insertLog(Log log);
    Log queryLog(String hostname, String file);
    void updateLog(Log log);
}
