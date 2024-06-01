package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.fegin.pojo.Result;
import org.example.fegin.pojo.dto.CpuMemInfoDTO;
import org.example.fegin.pojo.dto.CpuMemQueryDTO;
import org.example.fegin.pojo.vo.CpuMemQueryVO;
import org.example.service.CpuMemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
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
    public Result<Void> upload(@RequestBody CpuMemInfoDTO cpuMemInfoDTO) {
        return cpuMemService.upload(cpuMemInfoDTO);
    }

    @GetMapping("/query")
    public Result<List<CpuMemQueryVO>> query(@SpringQueryMap CpuMemQueryDTO cpuMemQueryDTO) {
        return cpuMemService.query(cpuMemQueryDTO);
    }

}
