package com.xiaomi.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaomi.server.Entity.Metric;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MetricMapper extends BaseMapper<Metric> {
    @Select("<script>" +
            "SELECT * FROM Metric WHERE endpoint = #{endpoint} " +
            "<if test='metric != null'>AND metric = #{metric}</if> " +
            "AND timestamp BETWEEN #{startTs} AND #{endTs}" +
            "</script>")
    List<Metric> queryMetrics(@Param("endpoint") String endpoint,
                              @Param("metric") String metric,
                              @Param("startTs") long startTs,
                              @Param("endTs") long endTs);

}

