package com.server.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.entity.Metric;

public interface MetricMapper extends BaseMapper<Metric>{

	@Select("SELECT * FROM t_metric ORDER BY TIMESTAMP DESC LIMIT 0,10")
	List<Metric> getMetric();
}
