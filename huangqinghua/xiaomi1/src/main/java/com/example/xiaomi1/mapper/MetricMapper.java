package com.example.xiaomi1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.xiaomi1.entity.Metric;
import com.example.xiaomi1.entity.MetricData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MetricMapper extends BaseMapper<Metric> {
    List<MetricData> getMetrics(
            @Param("endpoint") String endpoint,
            @Param("metric") String metric,
            @Param("start_ts") long start_ts,
            @Param("end_ts") long end_ts);
}
