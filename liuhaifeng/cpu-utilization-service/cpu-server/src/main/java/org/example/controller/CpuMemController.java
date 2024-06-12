package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.fegin.pojo.Result;
import org.example.fegin.pojo.dto.CpuMemInfoDTO;
import org.example.fegin.pojo.dto.CpuMemQueryDTO;
import org.example.fegin.pojo.vo.CpuMemQueryVO;
import org.example.service.CpuMemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.SpringQueryMap;
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
public class CpuMemController {

    @Autowired
    private CpuMemService cpuMemService;

    @PostMapping("/upload")
    @CacheEvict(cacheNames = "cpuMemCache*", allEntries = true)
    public Result<Void> upload(@RequestBody List<CpuMemInfoDTO> cpuMemInfoDTOList) {
        log.info("cpu-service 接收到数据：{}", cpuMemInfoDTOList);
        cpuMemService.upload(cpuMemInfoDTOList);
        return Result.success();
    }

    @GetMapping("/query")
    @Cacheable(cacheNames = "cpuMemCache", key = "#cpuMemQueryDTO.endpoint + #cpuMemQueryDTO.metric + #cpuMemQueryDTO.startTs + '-' + #cpuMemQueryDTO.endTs")
    public Result<List<CpuMemQueryVO>> query(@SpringQueryMap CpuMemQueryDTO cpuMemQueryDTO) {
        log.info("查询主机信息：{}", cpuMemQueryDTO);
        List<CpuMemQueryVO> query = cpuMemService.query(cpuMemQueryDTO);
        return Result.success(query);
    }

}
