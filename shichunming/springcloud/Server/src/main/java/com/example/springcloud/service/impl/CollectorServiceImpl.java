package com.example.springcloud.service.impl;

import com.example.springcloud.base.MyException;
import com.example.springcloud.base.SnowflakeIdGenerator;
import com.example.springcloud.base.enums.ErrorCode;
import com.example.springcloud.base.enums.MetricEnum;
import com.example.springcloud.controller.request.MetricUploadRequest;
import com.example.springcloud.controller.request.MetricQueryRequest;
import com.example.springcloud.controller.response.MetricQueryResponse;
import com.example.springcloud.mapper.XmCollectorMapper;
import com.example.springcloud.po.XmCollectorPo;
import com.example.springcloud.service.CollectorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @ClassName CollectorServiceImpl
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-04 23:40
 **/
@Slf4j
@Service
public class CollectorServiceImpl implements CollectorService {
    @Resource
    private XmCollectorMapper xmCollectorMapper;
    @Autowired
    private SnowflakeIdGenerator snowflakeIdGenerator;

    @Override
    public void metricUpload(MetricUploadRequest request) {
        save(request);
    }

    @Override
    public List<MetricQueryResponse> queryMetric(MetricQueryRequest request) {
        if (request.getStart_ts() == null || request.getEnd_ts() == null) {
            throw new MyException(ErrorCode.PARAM_ERROR, "时间参数为空");
        }
        if (Objects.nonNull(request.getMetric())) {
            if (MetricEnum.check(request.getMetric())) {
                throw new MyException(ErrorCode.PARAM_ERROR, "metric参数错误");
            }
        }
        List<XmCollectorPo> poList = xmCollectorMapper.selectByEndpointAndMetricAndTime(request.getEndpoint(), request.getMetric(), request.getStart_ts(), request.getEnd_ts());
        Map<String, MetricQueryResponse> resultMap = new HashMap<>();
        poList.stream().forEach(po -> {
            String currentMetric = po.getMetric();
            MetricQueryResponse metricResponse = resultMap.computeIfAbsent(currentMetric, k -> {
                MetricQueryResponse newResponse = new MetricQueryResponse();
                newResponse.setMetric(k); // 明确设置metric属性
                return newResponse;
            });
            metricResponse.getValues().add(new MetricQueryResponse.Value(po.getTimestamp(), po.getValue()));
        });

        return new ArrayList<>(resultMap.values());
    }

    public void save(MetricUploadRequest request) {
        XmCollectorPo po = new XmCollectorPo();
        po.setId(snowflakeIdGenerator.nextId());
        po.setEndpoint(request.getEndpoint());
        po.setMetric(request.getMetric());
        po.setStep(request.getStep());
//        long longTimestamp = request.getTimestamp();
//        System.out.println(longTimestamp);
//        java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(longTimestamp);
        po.setTimestamp(request.getTimestamp());
        po.setValue(request.getValue());
        xmCollectorMapper.insert(po);
        log.info("\n----------------------\n" +
                "保存成功:"+po.getEndpoint()+po.getMetric()+po.getTimestamp());
    }
}
