package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.fegin.clients.CpuMemClient;
import org.example.fegin.pojo.Result;
import org.example.fegin.pojo.dto.CpuMemInfoDTO;
import org.example.fegin.pojo.dto.CpuMemQueryDTO;
import org.example.fegin.pojo.vo.CpuMemQueryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liuhaifeng
 * @date 2024/05/30/23:10
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class Controller {

    @Autowired
    private CpuMemClient cpuMemClient;

    @PostMapping("/upload")
    public Result<Void> upload(@RequestBody List<CpuMemInfoDTO> cpuMemInfoDTOList) {
        log.info("cpuMemInfoDTOList:{}", cpuMemInfoDTOList);
        return cpuMemClient.upload(cpuMemInfoDTOList);
    }

    @GetMapping("/query")
    public Result<List<CpuMemQueryVO>> query(CpuMemQueryDTO cpuMemQueryDTO) {
        log.info("cpuMemQueryDTO:{}", cpuMemQueryDTO);
        return cpuMemClient.query(cpuMemQueryDTO);
    }
}
