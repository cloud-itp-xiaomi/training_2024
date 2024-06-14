package com.example.demo01.mapper;

import com.example.demo01.bean.DataItem;
import com.example.demo01.bean.MetricVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DataMapper {
    //插入新对象
    int insert(DataItem record);

    //查询所有符合条件的记录
    List<MetricVO> selectByCondition(@Param("endpoint") String endpoint, @Param("metric") String metric,
                                     @Param("startTime") Long startTime, @Param("endTime") Long endTime);

    //limit限制记录条数
    List<MetricVO> selectByLimit(@Param("metric") String metric, @Param("limit") int limit);

}
