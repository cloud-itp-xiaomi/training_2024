package org.example.service;

import org.example.fegin.pojo.dto.UtilizationUploadDTO;
import org.example.fegin.pojo.vo.UtilizationQueryVO;

import java.util.List;

/**
 * cpu内存利用率服务类
 *
 * @author liuhaifeng
 * @date 2024/05/30/21:27
 */
public interface UtilizationService {

    void upload(List<UtilizationUploadDTO> utilizationUploadDTOList);

    List<UtilizationQueryVO> query(String endpointName, String metric, Long startTs, Long endTs);
}
