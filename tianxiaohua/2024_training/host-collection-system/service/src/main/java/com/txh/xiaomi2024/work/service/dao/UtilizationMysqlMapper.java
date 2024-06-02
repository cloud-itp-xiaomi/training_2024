package com.txh.xiaomi2024.work.service.dao;

import com.txh.xiaomi2024.work.service.bean.Utilization;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 数据库操作映射
 * 与.xml文件相呼应
 */
@Mapper
public interface UtilizationMysqlMapper {
    void createTable(String tableName);
    List<Utilization> queryAll();
    void addData(Utilization utilization);
    List<Utilization> queryByTimestamp(long start, long end);
    List<Utilization> queryByMetric(String metric);
    List<Utilization> queryByeEndpoint(String endpoint);
    List<Utilization> query(String endpoint, String metric, long start, long end);
    List<Utilization> queryByEMS(String endpoint, String metric, long start);
}
