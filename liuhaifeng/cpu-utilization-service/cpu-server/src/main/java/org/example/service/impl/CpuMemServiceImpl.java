package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.common.utils.TimeFormatUtil;
import org.example.enums.MetricTypeEnum;
import org.example.exception.BaseException;
import org.example.fegin.pojo.Result;
import org.example.fegin.pojo.dto.CpuMemInfoDTO;
import org.example.fegin.pojo.dto.CpuMemQueryDTO;
import org.example.fegin.pojo.vo.CpuMemQueryVO;
import org.example.mapper.CpuMemInfoMapper;
import org.example.mapper.EndpointMapper;
import org.example.pojo.entity.CpuMemInfo;
import org.example.pojo.entity.Endpoint;
import org.example.service.CpuMemService;
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
public class CpuMemServiceImpl implements CpuMemService {

    @Autowired
    private CpuMemInfoMapper cpuMemInfoMapper;

    @Autowired
    private EndpointMapper endpointMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void upload(List<CpuMemInfoDTO> cpuMemInfoDTOList) {
        if (CollectionUtils.isEmpty(cpuMemInfoDTOList)) {
            throw new BaseException("收集到的数据为空");
        }

        List<CpuMemInfo> cpuMemInfoList = new ArrayList<>();
        cpuMemInfoDTOList.forEach(cpuMemDTO -> {
            Endpoint endpoint = endpointMapper.getEndpointByName(cpuMemDTO.getEndpoint());
            Integer endpointId = endpoint != null ? endpoint.getId() : null;
            if (endpointId == null) {
                //新的主机名，插入
                Endpoint saveEndpoint = Endpoint.builder()
                        .name(cpuMemDTO.getEndpoint())
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .deleted(0)
                        .build();
                endpointMapper.insert(saveEndpoint);
                endpointId = saveEndpoint.getId();
            }
            CpuMemInfo cpuMemInfo = CpuMemInfo.builder()
                    .metric(cpuMemDTO.getMetric())
                    .endpointId(endpointId)
                    .timestamp(TimeFormatUtil.longToLocalDateTime(cpuMemDTO.getTimestamp()))
                    .step(cpuMemDTO.getStep())
                    .value(cpuMemDTO.getValue())
                    .tags(cpuMemDTO.getTags())
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .deleted(0)
                    .build();
            MetricTypeEnum metricTypeEnum = MetricTypeEnum.getByValue(cpuMemDTO.getMetric());
            if (metricTypeEnum == null) {
                throw new BaseException("指标类型不存在");
            }
            cpuMemInfo.setMetricType(metricTypeEnum.getCode());
            cpuMemInfoList.add(cpuMemInfo);
        });
        cpuMemInfoMapper.insertBatch(cpuMemInfoList);
    }


    @Override
    public List<CpuMemQueryVO> query(CpuMemQueryDTO cpuMemQueryDTO) {
        Endpoint endpoint = endpointMapper.getEndpointByName(cpuMemQueryDTO.getEndpoint());
        if (endpoint == null) {
            throw new BaseException("查询的主机不存在");
        }
        if (!StringUtils.isEmpty(cpuMemQueryDTO.getMetric())) {
            MetricTypeEnum metricTypeEnum = MetricTypeEnum.getByValue(cpuMemQueryDTO.getMetric());
            if (metricTypeEnum == null) {
                throw new BaseException("查询的指标类型不存在");
            }
            List<CpuMemInfo> cpuMemInfoList = cpuMemInfoMapper.query(endpoint.getId(),
                    TimeFormatUtil.longToLocalDateTime(cpuMemQueryDTO.getStartTs()),
                    TimeFormatUtil.longToLocalDateTime(cpuMemQueryDTO.getEndTs()),
                    metricTypeEnum.getCode(),
                    0);
            log.info("查询到的数据：{}", cpuMemInfoList);
            List<CpuMemQueryVO> result = new ArrayList<>();
            CpuMemQueryVO cpuMemQueryVO = new CpuMemQueryVO();
            List<CpuMemQueryVO.Value> valueList = new ArrayList<>();
            cpuMemQueryVO.setMetric(metricTypeEnum.getValue());
            cpuMemInfoList.forEach(cpuMemInfo -> {
                CpuMemQueryVO.Value value = new CpuMemQueryVO.Value();
                value.setTimestamp(Timestamp.valueOf(cpuMemInfo.getTimestamp()).getTime());
                value.setValue(cpuMemInfo.getValue());
                valueList.add(value);
            });
            cpuMemQueryVO.setValues(valueList);
            result.add(cpuMemQueryVO);
            return result;
        } else {
            //查询所有指标
            List<CpuMemInfo> cpuMemInfoList = cpuMemInfoMapper.query(endpoint.getId(),
                    TimeFormatUtil.longToLocalDateTime(cpuMemQueryDTO.getStartTs()),
                    TimeFormatUtil.longToLocalDateTime(cpuMemQueryDTO.getEndTs()),
                    null,
                    0);
            List<CpuMemQueryVO> result = new ArrayList<>();
            CpuMemQueryVO cpuQueryVO = new CpuMemQueryVO();
            cpuQueryVO.setMetric(MetricTypeEnum.CPU_USED_PERCENT.getValue());
            CpuMemQueryVO memQueryVO = new CpuMemQueryVO();
            memQueryVO.setMetric(MetricTypeEnum.MEM_USED_PERCENT.getValue());
            List<CpuMemQueryVO.Value> cpuValueList = new ArrayList<>();
            List<CpuMemQueryVO.Value> memValueList = new ArrayList<>();

            cpuMemInfoList.forEach(cpuMemInfo -> {
                CpuMemQueryVO.Value value = new CpuMemQueryVO.Value();
                value.setTimestamp(TimeFormatUtil.localDateTimeToLong(cpuMemInfo.getTimestamp()));
                value.setValue(cpuMemInfo.getValue());
                if (cpuMemInfo.getMetricType() == 1) {
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
