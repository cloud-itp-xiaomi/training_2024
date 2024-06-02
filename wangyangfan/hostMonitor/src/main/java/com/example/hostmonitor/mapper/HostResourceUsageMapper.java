package com.example.hostmonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.hostmonitor.pojo.HostResourceUsageEntity;
import com.example.hostmonitor.pojo.QueryData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface HostResourceUsageMapper extends BaseMapper<HostResourceUsageEntity> {

    @Select("SELECT timestamp, value " +
            "FROM resource_usage " +
            "WHERE endpoint = #{endpoint} AND metric = #{metric} AND timestamp BETWEEN #{start_ts} AND #{end_ts}")
    List<QueryData.TimeWithValue> selectByTimestamp(@Param("endpoint") String endpoint,
                                                    @Param("metric") String metric,
                                                    @Param("start_ts") Long start_ts,
                                                    @Param("end_ts") Long end_ts);

//    @Select("SELECT timestamp, value " +
//            "FROM resource_usage " +
//            "WHERE endpoint = #{endpoint}AND timestamp BETWEEN #{start_ts} AND #{end_ts}")
//    List<QueryData.TimeWithValue> selectWithoutMetricByTimestamp(@Param("endpoint") String endpoint,
//                                                    @Param("start_ts") Long start_ts,
//                                                    @Param("end_ts") Long end_ts);
//
//    @Select("SELECT distinct metric " +
//            "FROM resource_usage " +
//            "WHERE endpoint = #{endpoint} AND timestamp BETWEEN #{start_ts} AND #{end_ts}")
//    List<String> getMetricList(@Param("endpoint") String endpoint,
//                               @Param("start_ts") Long start_ts,
//                               @Param("end_ts") Long end_ts);
}
