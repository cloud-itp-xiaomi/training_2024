package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.fegin.clients.LogClient;
import org.example.fegin.pojo.Result;
import org.example.fegin.pojo.dto.LogUploadDTO;
import org.example.service.LogCollectorService;
import org.example.utils.ExecuteSellCommandUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 日志采集服务实现类
 *
 * @author liuhaifeng
 * @date 2024/06/13/22:33
 */
@Slf4j
@Service
public class LogCollectorServiceImpl implements LogCollectorService {

    @Autowired
    private LogClient logClient;

    @Override
    public void upload(List<LogUploadDTO> logUploadDTOList) {
        String hostname = ExecuteSellCommandUtil.getEndpoint();
        for (LogUploadDTO logUploadDTO : logUploadDTOList) {
            logUploadDTO.setHostname(hostname);
        }
        Result<Void> upload = logClient.upload(logUploadDTOList);
        if (upload.getCode() != 1) {
            log.error("日志上传失败");
        }
    }
}
