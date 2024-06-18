package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.fegin.pojo.Result;
import org.example.fegin.pojo.dto.LogQueryDTO;
import org.example.fegin.pojo.dto.LogUploadDTO;
import org.example.fegin.pojo.vo.LogQueryVO;
import org.example.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 日志相关接口
 *
 * @author liuhaifeng
 * @date 2024/06/05/16:17
 */
@Slf4j
@RequestMapping("/api/log")
@RestController
public class LogController {

    @Autowired
    private LogService logService;

    @PostMapping("/upload")
    public Result<Void> upload(@RequestBody List<LogUploadDTO> uploadDTOList) {
        log.info("日志上传：{}", uploadDTOList);
        logService.upload(uploadDTOList);
        return Result.success();
    }

    @GetMapping("/query")
    public Result<LogQueryVO> query(@SpringQueryMap LogQueryDTO logQueryDTO) {
        log.info("日志查询：{}", logQueryDTO);
        LogQueryVO logQueryVO = logService.query(logQueryDTO);
        return Result.success(logQueryVO);
    }
}
