package com.cl.server.service.impl;

import com.cl.server.entity.CpuStatus;
import com.cl.server.entity.CpuStatusResp;
import com.cl.server.entity.Values;
import com.cl.server.mapper.CpuStatusDao;
import com.cl.server.service.CpuStatusService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * (CpuStatus)表服务实现类
 *
 * @author tressures
 * @date:  2024-05-26 17:06:02
 */
@Service("cpuStatusService")
public class CpuStatusServiceImpl implements CpuStatusService {
    @Resource
    private CpuStatusDao cpuStatusDao;


    @Override
    public void uploadMetrics(List<CpuStatus> cpuStatusList) {
        cpuStatusDao.insertBatch(cpuStatusList);
    }

    @Override
        public List<CpuStatusResp> queryMetrics(String endpoint, String metric, Long start_ts, Long end_ts) {
        //根据机器及指标查出所有
        CpuStatus cpuStatus = new CpuStatus();
        cpuStatus.setEndpoint(endpoint);
        cpuStatus.setMetric(metric);
        List<CpuStatus> cpuStatusList = cpuStatusDao.queryAllByLimit(cpuStatus);
        List<CpuStatusResp> cpuStatusRespList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(cpuStatusList)) {
            //过滤不符合时间段的
            List<CpuStatus> cpuStatuses = cpuStatusList.stream()
                    .filter(item -> item.getTimestamp() >= start_ts && item.getTimestamp() <= end_ts)
                    .collect(Collectors.toList());
            //根据指标分组
            Map<String,List<CpuStatus>> map = cpuStatuses.stream()
                    .collect(Collectors.groupingBy(CpuStatus::getMetric));
            //包装data
            for(String key:map.keySet()){
                List<CpuStatus> cs = map.get(key);
                List<Values> values = cs.stream().map(item -> {
                            Values value = new Values();
                            value.setTimeStamp(item.getTimestamp());
                            value.setValue(item.getValue());
                            return value;
                        })
                        .collect(Collectors.toList());
                CpuStatusResp cpuStatusResp = new CpuStatusResp();
                cpuStatusResp.setMetric(key);
                cpuStatusResp.setValues(values);
                cpuStatusRespList.add(cpuStatusResp);
            }
        }
        return cpuStatusRespList;
    }
}
