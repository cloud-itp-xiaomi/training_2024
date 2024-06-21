package org.example.service;

import org.example.fegin.pojo.dto.LogQueryDTO;
import org.example.fegin.pojo.dto.LogUploadDTO;
import org.example.fegin.pojo.vo.LogQueryVO;

import java.util.List;

/**
 * 日志服务类
 *
 * @author liuhaifeng
 * @date 2024/06/05/16:51
 */
public interface LogService {
    /**
     * 日志上传
     *
     * @param logUploadDTOList
     */
    void upload(List<LogUploadDTO> logUploadDTOList);

    /**
     * 日志查询
     *
     * @param logQueryDTO
     * @return
     */
    LogQueryVO query(LogQueryDTO logQueryDTO);
}
