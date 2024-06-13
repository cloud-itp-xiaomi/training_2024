package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.common.utils.TimeFormatUtil;
import org.example.enums.MetricTypeEnum;
import org.example.exception.BaseException;
import org.example.fegin.pojo.dto.UtilizationUploadDTO;
import org.example.fegin.pojo.vo.UtilizationQueryVO;
import org.example.mapper.EndpointMapper;
import org.example.mapper.UtilizationMapper;
import org.example.pojo.entity.Endpoint;
import org.example.pojo.entity.Utilization;
import org.example.service.UtilizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * cpu内存利用率服务类
 *
 * @author liuhaifeng
 * @date 2024/05/29/15:24
 */
@Slf4j
@Service
public class UtilizationServiceImpl implements UtilizationService {

    @Autowired
    private UtilizationMapper utilizationMapper;

    @Autowired
    private EndpointMapper endpointMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void upload(List<UtilizationUploadDTO> utilizationUploadDTOList) {
        if (CollectionUtils.isEmpty(utilizationUploadDTOList)) {
            throw new BaseException("收集到的数据为空");
        }

        List<Utilization> saveList = new ArrayList<>();
        utilizationUploadDTOList.forEach(uploadDTO -> {
            Endpoint endpoint = endpointMapper.getEndpointByName(uploadDTO.getEndpoint());
            Integer endpointId = endpoint != null ? endpoint.getId() : null;
            if (endpointId == null) {
                //新的主机名，插入
                Endpoint saveEndpoint = Endpoint.builder()
                        .name(uploadDTO.getEndpoint())
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .deleted(0)
                        .build();
                endpointMapper.insert(saveEndpoint);
                endpointId = saveEndpoint.getId();
            }
            Utilization utilization = Utilization.builder()
                    .metric(uploadDTO.getMetric())
                    .endpointId(endpointId)
                    .timestamp(TimeFormatUtil.longToLocalDateTime(uploadDTO.getTimestamp()))
                    .step(uploadDTO.getStep())
                    .value(uploadDTO.getValue())
                    .tags(uploadDTO.getTags())
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .deleted(0)
                    .build();
            MetricTypeEnum metricTypeEnum = MetricTypeEnum.getByValue(uploadDTO.getMetric());
            if (metricTypeEnum == null) {
                throw new BaseException("指标类型不存在");
            }
            utilization.setMetricType(metricTypeEnum.getCode());
            saveList.add(utilization);
        });
        utilizationMapper.insertBatch(saveList);
    }

    @Override
    public List<UtilizationQueryVO> query(String endpointName, String metric, Long startTs, Long endTs) {
        if (startTs > endTs) {
            throw new BaseException("查询的开始时间不能大于结束时间");
        }
        Endpoint endpoint = endpointMapper.getEndpointByName(endpointName);
        if (endpoint == null) {
            throw new BaseException("查询的主机不存在");
        }
        if (!StringUtils.isEmpty(metric)) {
            MetricTypeEnum metricTypeEnum = MetricTypeEnum.getByValue(metric);
            if (metricTypeEnum == null) {
                throw new BaseException("查询的指标类型不存在");
            }
            List<Utilization> utilizationList = utilizationMapper.query(endpoint.getId(),
                    TimeFormatUtil.longToLocalDateTime(startTs),
                    TimeFormatUtil.longToLocalDateTime(endTs),
                    metricTypeEnum.getCode(),
                    0);
            List<UtilizationQueryVO> result = new ArrayList<>();
            UtilizationQueryVO utilizationQueryVO = new UtilizationQueryVO();
            List<UtilizationQueryVO.Value> valueList = new ArrayList<>();
            utilizationQueryVO.setMetric(metricTypeEnum.getValue());
            utilizationList.forEach(utilization -> {
                UtilizationQueryVO.Value value = new UtilizationQueryVO.Value();
                value.setTimestamp(Timestamp.valueOf(utilization.getTimestamp()).getTime());
                value.setValue(utilization.getValue());
                valueList.add(value);
            });
            utilizationQueryVO.setValues(valueList);
            result.add(utilizationQueryVO);
            return result;
        } else {
            //查询所有指标
            List<Utilization> utilizationList = utilizationMapper.query(endpoint.getId(),
                    TimeFormatUtil.longToLocalDateTime(startTs),
                    TimeFormatUtil.longToLocalDateTime(endTs),
                    null,
                    0);
            List<UtilizationQueryVO> result = new ArrayList<>();
            UtilizationQueryVO cpuQueryVO = new UtilizationQueryVO();
            cpuQueryVO.setMetric(MetricTypeEnum.CPU_USED_PERCENT.getValue());
            UtilizationQueryVO memQueryVO = new UtilizationQueryVO();
            memQueryVO.setMetric(MetricTypeEnum.MEM_USED_PERCENT.getValue());
            List<UtilizationQueryVO.Value> cpuValueList = new ArrayList<>();
            List<UtilizationQueryVO.Value> memValueList = new ArrayList<>();
            utilizationList.forEach(utilization -> {
                UtilizationQueryVO.Value value = new UtilizationQueryVO.Value();
                value.setTimestamp(TimeFormatUtil.localDateTimeToLong(utilization.getTimestamp()));
                value.setValue(utilization.getValue());
                if (utilization.getMetricType() == 1) {
                    cpuValueList.add(value);
                } else {
                    memValueList.add(value);
                }
            });
            cpuQueryVO.setValues(cpuValueList);
            memQueryVO.setValues(memValueList);
            result.add(cpuQueryVO);
            result.add(memQueryVO);
            return result;
        }
    }
}
