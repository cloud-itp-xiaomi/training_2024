package com.example.springcloud.mapper;

import com.example.springcloud.po.XmCollectorPo;
import org.apache.ibatis.annotations.Param;


import java.util.List;



public interface XmCollectorMapper {
    /**
     *新增
     */
    int insert(XmCollectorPo po);
    /**
     * 根据endpoint和metric和时间戳查询
     */
    List<XmCollectorPo> selectByEndpointAndMetricAndTime(@Param("endpoint") String endpoint, @Param("metric") String metric, Long start_ts, Long end_ts);
}
