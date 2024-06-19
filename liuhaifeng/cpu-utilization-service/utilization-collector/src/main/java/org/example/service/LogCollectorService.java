package org.example.service;

import org.example.fegin.pojo.dto.LogUploadDTO;

import java.util.List;

/**
 * 日志采集服务接口
 *
 * @author liuhaifeng
 * @date 2024/06/13/22:32
 */
public interface LogCollectorService {

    void upload(List<LogUploadDTO> logUploadDTOList);
}
