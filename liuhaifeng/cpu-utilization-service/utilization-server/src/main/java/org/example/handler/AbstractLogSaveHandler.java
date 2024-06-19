package org.example.handler;

import org.example.fegin.pojo.dto.LogQueryDTO;
import org.example.fegin.pojo.dto.LogUploadDTO;
import org.example.fegin.pojo.vo.LogQueryVO;

import java.util.List;

/**
 * 日志保存处理器
 *
 * @author liuhaifeng
 * @date 2024/06/09/17:24
 */
public interface AbstractLogSaveHandler {

    /**
     * 日志上传
     *
     * @param logUploadDTOList
     */
    void save(List<LogUploadDTO> logUploadDTOList);

    /**
     * 日志查询
     *
     * @param logQueryDTO
     * @return
     */
    LogQueryVO query(LogQueryDTO logQueryDTO);
}
