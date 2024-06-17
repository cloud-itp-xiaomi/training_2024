package org.qiaojingjing.server.service;

import org.qiaojingjing.server.pojo.dto.MetricDTO;
import org.qiaojingjing.server.pojo.dto.MetricsDTO;
import org.qiaojingjing.server.pojo.vo.MetricVO;

import java.util.List;

public interface ComputerService {
    void upload(List<MetricsDTO> metricsDTO);

    List<MetricVO> query(MetricDTO metricDTO);
}
