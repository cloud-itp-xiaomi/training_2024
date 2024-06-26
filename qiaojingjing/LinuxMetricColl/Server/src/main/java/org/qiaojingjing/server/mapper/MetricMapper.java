package org.qiaojingjing.server.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.qiaojingjing.server.pojo.dto.MetricDTO;
import org.qiaojingjing.server.pojo.entity.Metric;
import org.qiaojingjing.server.pojo.vo.MetricVO;

import java.util.List;

@Mapper
public interface MetricMapper {

    void save(List<Metric> metrics);

    List<MetricVO> query(MetricDTO metricDTO);
}
