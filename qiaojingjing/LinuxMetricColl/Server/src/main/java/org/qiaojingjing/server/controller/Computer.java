package org.qiaojingjing.server.controller;


import lombok.extern.slf4j.Slf4j;
import org.qiaojingjing.server.pojo.dto.MetricDTO;
import org.qiaojingjing.server.pojo.dto.MetricsDTO;
import org.qiaojingjing.server.pojo.vo.MetricVO;
import org.qiaojingjing.server.result.Result;
import org.qiaojingjing.server.service.ComputerService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/metric")
@Slf4j
public class Computer {
    @Resource
    private ComputerService computerService;

    /**
     * 上报接口
     * @version 0.1.0
     * @author qiaojingjing
     * @since 0.1.0
     **/
    @PostMapping("/upload")
    public Result uploadMetrics(@RequestBody List<MetricsDTO> metricsDTO){
        log.info("接收到Collector上传的数据...");
        computerService.upload(metricsDTO);
        return Result.success();
    }

    @GetMapping("/query")
    public Result query(@RequestParam String endpoint,
                        @RequestParam(required = false) String metric,
                        @RequestParam("start_ts") Long startTs,
                        @RequestParam("end_ts")Long endTs){
        MetricDTO metricDTO = new MetricDTO(endpoint,metric,startTs,endTs);
        log.info("查询指标:{}",metricDTO);
        List<MetricVO> list = computerService.query(metricDTO);
        return Result.success(list);
    }
}
