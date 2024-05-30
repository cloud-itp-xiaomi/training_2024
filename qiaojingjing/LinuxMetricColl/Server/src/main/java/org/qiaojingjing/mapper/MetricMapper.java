package org.qiaojingjing.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.qiaojingjing.pojo.dto.MetricDTO;
import org.qiaojingjing.pojo.entity.Metric;
import org.qiaojingjing.pojo.vo.MetricVO;

import java.util.List;

@Mapper
public interface MetricMapper {

    void save(List<Metric> metrics);

    List<MetricVO> query(MetricDTO metricDTO);
}
