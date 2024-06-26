package com.h_s.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.h_s.entity.Metric;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MetricMapper extends BaseMapper<Metric> {

    @Select("select * from metric where endpoint = #{endpoint} and metric = #{metric} and timestamp between #{start_ts} and #{end_ts}")
    List<Metric> queryMapperMetricsAll(@Param("endpoint") String endpoint,
                                       @Param("metric") String metric,
                                       @Param("start_ts") long startTs,
                                       @Param("end_ts") long endTs);

    @Select("select * from metric where endpoint = #{endpoint}  and timestamp between #{start_ts} and #{end_ts}")
    List<Metric> queryMapperMetrics(@Param("endpoint") String endpoint,
                                    @Param("start_ts") long startTs,
                                    @Param("end_ts") long endTs);


}
