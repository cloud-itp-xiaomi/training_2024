package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.fegin.pojo.Result;
import org.example.fegin.pojo.dto.UtilizationUploadDTO;
import org.example.fegin.pojo.vo.UtilizationQueryVO;
import org.example.service.UtilizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * cpu和内存利用率相关接口
 *
 * @author liuhaifeng
 * @date 2024/05/30/21:20
 */

@Slf4j
@RequestMapping("/api/metric")
@RestController
public class UtilizationController {

    @Autowired
    private UtilizationService utilizationService;

    @PostMapping("/upload")
    @CacheEvict(cacheNames = "cpuMemCache*", allEntries = true)
    public Result<Void> upload(@RequestBody List<UtilizationUploadDTO> utilizationUploadDTOList) {
        log.info("接收到数据：{}", utilizationUploadDTOList);
        utilizationService.upload(utilizationUploadDTOList);
        return Result.success();
    }

    @GetMapping("/query")
    @Cacheable(cacheNames = "cpuMemCache", key = "#endpoint + #metric + #startTs + '-' + #endTs")
    public Result<List<UtilizationQueryVO>> query(@RequestParam("endpoint") String endpoint,
                                                  @RequestParam(value = "metric", required = false) String metric,
                                                  @RequestParam("start_ts") Long startTs,
                                                  @RequestParam("end_ts") Long endTs) {
        log.info("查询主机信息：{}, {}, {}, {}", endpoint, metric, startTs, endTs);
        List<UtilizationQueryVO> query = utilizationService.query(endpoint, metric, startTs, endTs);
        return Result.success(query);
    }
}
