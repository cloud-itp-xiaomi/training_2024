package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.pojo.entity.CpuMemInfo;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author liuhaifeng
 * @date 2024/05/29/17:45
 */
@Mapper
public interface CpuMemInfoMapper {
    void insertBatch(@Param("cpuMemInfoList") List<CpuMemInfo> cpuMemInfoList);

    List<CpuMemInfo> query(@Param("endpointId") Integer endpointId,
                           @Param("startTs") LocalDateTime startTs,
                           @Param("endTs") LocalDateTime endTs,
                           @Param("metricType") Integer metricType);
}
