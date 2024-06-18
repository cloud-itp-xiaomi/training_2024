package org.example.fegin.clients;

import org.example.fegin.pojo.Result;
import org.example.fegin.pojo.dto.LogQueryDTO;
import org.example.fegin.pojo.dto.LogUploadDTO;
import org.example.fegin.pojo.vo.LogQueryVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 日志相关接口
 *
 * @author liuhaifeng
 * @date 2024/06/05/16:27
 */
@FeignClient(name = "server", contextId = "logClient")
public interface LogClient {

    /**
     * 上传日志
     * @param uploadDTOList
     * @return
     */
    @PostMapping("api/log/upload")
    Result<Void> upload(@RequestBody List<LogUploadDTO> uploadDTOList);

    /**
     * 查询日志
     * @param logQueryDTO
     * @return
     */
    @GetMapping("api/log/query")
    Result<LogQueryVO> query(LogQueryDTO logQueryDTO);
}
