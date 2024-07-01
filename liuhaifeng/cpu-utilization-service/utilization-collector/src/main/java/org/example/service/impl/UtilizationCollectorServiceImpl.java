package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.fegin.clients.UtilizationClient;
import org.example.fegin.pojo.Result;
import org.example.fegin.pojo.dto.UtilizationUploadDTO;
import org.example.service.UtilizationCollectorService;
import org.example.utils.ExecuteSellCommandUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 利用率采集服务实现类
 *
 * @author liuhaifeng
 * @date 2024/06/02/14:57
 */
@Slf4j
@Service
public class UtilizationCollectorServiceImpl implements UtilizationCollectorService {

    @Autowired
    private UtilizationClient utilizationClient;

    @Override
    public void upload() {
        String endpoint = ExecuteSellCommandUtil.getEndpoint();
        log.info("采集到主机名称:{}", endpoint);
        Double cpuUsed = ExecuteSellCommandUtil.getCpuUsed();
        log.info("采集到CPU利用率:{}", cpuUsed);
        Double memUsed = ExecuteSellCommandUtil.getMemUsed();
        log.info("采集到内存利用率:{}", memUsed);
        UtilizationUploadDTO cpuUploadDTO = new UtilizationUploadDTO();
        cpuUploadDTO.setEndpoint(endpoint);
        cpuUploadDTO.setMetric("cpu.used.percent");
        cpuUploadDTO.setValue(cpuUsed);
        cpuUploadDTO.setTimestamp(System.currentTimeMillis() / 1000);
        cpuUploadDTO.setStep(60);
        UtilizationUploadDTO memUploadDTO = new UtilizationUploadDTO();
        memUploadDTO.setEndpoint(endpoint);
        memUploadDTO.setMetric("mem.used.percent");
        memUploadDTO.setValue(memUsed);
        memUploadDTO.setTimestamp(System.currentTimeMillis() / 1000);
        memUploadDTO.setStep(60);
        List<UtilizationUploadDTO> utilizationUploadDTOList = new ArrayList<>();
        utilizationUploadDTOList.add(cpuUploadDTO);
        utilizationUploadDTOList.add(memUploadDTO);
        Result<Void> upload = utilizationClient.upload(utilizationUploadDTOList);
        if (upload.getCode() != 1) {
            log.error("上传失败");
        }
    }
}
