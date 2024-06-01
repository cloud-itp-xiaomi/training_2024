package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
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
    public Result<Void> upload(CpuMemInfoDTO cpuMemInfoDTO) {
        log.info("cpu-service 接收到数据：{}", cpuMemInfoDTO);
        if (cpuMemInfoDTO == null && !CollectionUtils.isEmpty(cpuMemInfoDTO.getCpuMems())) {
            throw new BaseException("收集到的数据为空");
        }

        List<CpuMemInfo> cpuMemInfoList = new ArrayList<>();
        cpuMemInfoDTO.getCpuMems().forEach(cpuMem -> {
            Endpoint endpoint = endpointMapper.getEndpointByName(cpuMem.getEndpoint());
            Integer endpointId = endpoint != null ? endpoint.getId() : null;
            if (endpointId == null) {
                //新的主机名，插入
                Endpoint saveEndpoint = Endpoint.builder()
                        .name(cpuMem.getEndpoint())
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .deleted(0)
                        .build();
                endpointMapper.insert(saveEndpoint);
                endpointId = saveEndpoint.getId();
            }
            CpuMemInfo cpuMemInfo = CpuMemInfo.builder()
                    .metric(cpuMem.getMetric())
                    .endpointId(endpointId)
                    .timestamp(cpuMem.getTimestamp())
                    .step(cpuMem.getStep())
                    .value(cpuMem.getValue())
                    .tags(cpuMem.getTags())
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .deleted(0)
                    .build();
            MetricTypeEnum metricTypeEnum = MetricTypeEnum.getByValue(cpuMem.getMetric());
            if (metricTypeEnum == null) {
                throw new BaseException("指标类型不存在");
            }
            cpuMemInfo.setMetricType(metricTypeEnum.getCode());
            cpuMemInfoList.add(cpuMemInfo);
        });
        cpuMemInfoMapper.insertBatch(cpuMemInfoList);

        return Result.success();
    }

    @Override
    public Result<List<CpuMemQueryVO>> query(CpuMemQueryDTO cpuMemQueryDTO) {
        log.info("查询主机信息：{}", cpuMemQueryDTO);
        Endpoint endpoint = endpointMapper.getEndpointByName(cpuMemQueryDTO.getEndpoint());
        if (endpoint == null) {
            throw new BaseException("查询的主机不存在");
        }
        if (!StringUtils.isEmpty(cpuMemQueryDTO.getMetric())) {
            MetricTypeEnum metricTypeEnum = MetricTypeEnum.getByValue(cpuMemQueryDTO.getMetric());
            if (metricTypeEnum == null) {
                throw new BaseException("查询的指标类型不存在");
            }
            List<CpuMemInfo> cpuMemInfoList = cpuMemInfoMapper.query(endpoint.getId(), cpuMemQueryDTO.getStartTs(), cpuMemQueryDTO.getEndTs(), metricTypeEnum.getCode());
            List<CpuMemQueryVO> result = new ArrayList<>();
            CpuMemQueryVO cpuMemQueryVO = new CpuMemQueryVO();
            List<CpuMemQueryVO.Value> valueList = new ArrayList<>();
            cpuMemQueryVO.setMetric(metricTypeEnum.getValue());
            cpuMemInfoList.forEach(cpuMemInfo -> {
                CpuMemQueryVO.Value value = new CpuMemQueryVO.Value();
                value.setTimestamp(cpuMemInfo.getTimestamp());
                value.setValue(cpuMemInfo.getValue());
                valueList.add(value);
            });
            cpuMemQueryVO.setValues(valueList);
            result.add(cpuMemQueryVO);
            return Result.success(result);
        } else {
            //查询所有指标
            List<CpuMemInfo> cpuMemInfoList = cpuMemInfoMapper.query(endpoint.getId(), cpuMemQueryDTO.getStartTs(), cpuMemQueryDTO.getEndTs(), null);
            List<CpuMemQueryVO> result = new ArrayList<>();
            CpuMemQueryVO cpuQueryVO = new CpuMemQueryVO();
            cpuQueryVO.setMetric(MetricTypeEnum.CPU_USED_PERCENT.getValue());
            CpuMemQueryVO memQueryVO = new CpuMemQueryVO();
            memQueryVO.setMetric(MetricTypeEnum.MEM_USED_PERCENT.getValue());
            List<CpuMemQueryVO.Value> cpuValueList = new ArrayList<>();
            List<CpuMemQueryVO.Value> memValueList = new ArrayList<>();

            cpuMemInfoList.forEach(cpuMemInfo -> {
                CpuMemQueryVO.Value value = new CpuMemQueryVO.Value();
                value.setTimestamp(cpuMemInfo.getTimestamp());
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
            return Result.success(result);

        }


    }
}
