package org.qiaojingjing.service;

import org.qiaojingjing.pojo.vo.MetricVO;
import org.qiaojingjing.pojo.dto.MetricDTO;
import org.qiaojingjing.pojo.dto.MetricsDTO;

import java.util.List;

public interface ComputerService {
    void upload(List<MetricsDTO> metricsDTO );

    List<MetricVO> query(MetricDTO metricDTO );
}
