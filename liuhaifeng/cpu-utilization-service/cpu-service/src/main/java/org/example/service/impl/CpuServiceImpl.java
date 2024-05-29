package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.service.CpuService;

/**
 * @author liuhaifeng
 * @date 2024/05/29/15:24
 */
@Slf4j
@DubboService
public class CpuServiceImpl implements CpuService {

    @Override
    public String getCpuInfo() {
        log.info("cpu:70%");
        return "cpu:70%";
    }
}
