package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.example.service.CpuService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuhaifeng
 * @date 2024/05/29/15:28
 */
@RestController
@RequestMapping
@Slf4j
public class CpuController {

    @DubboReference
    private CpuService cpuService;

    @GetMapping("info")
    public String getCpuInfo() {
        log.info("获取cpu信息");
        return cpuService.getCpuInfo();
    }

}
