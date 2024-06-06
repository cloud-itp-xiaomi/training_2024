package com.winter.mapper;

import com.winter.domain.Performance;
import com.winter.req.QueryPerformanceReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 持久层接口
 * */

@Mapper
public interface PerformanceMapper {
        /**
         * 查询所有的数据
         * */
        List<Performance> findAll() throws Exception;

        /**
         * 保存机器的信息
         * */
        void add(@Param("performance") Performance performance) throws Exception;

        /**
         * 条件查询机器信息
         * */
        List<Performance> findByCondition(@Param("req") QueryPerformanceReq req) throws Exception;
}
