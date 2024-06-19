package com.cl.server.controller;

import com.alibaba.fastjson.JSON;
import com.cl.server.config.LogStorageConfig;
import com.cl.server.pojo.DTO.LogInfoDTO;
import com.cl.server.pojo.DTO.LogQueryDTO;
import com.cl.server.entity.Result;
import com.cl.server.pojo.VO.LogInfoVO;
import com.cl.server.exception.BaseException;
import com.cl.server.service.LogStorageService;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
/**
 * LogStorage控制层
 *
 * @author tressures
 * @date  2024/6/5
 */
@Slf4j
@RestController
@RequestMapping("/api/log")
public class LogStorageController {

    @Resource
    private LogStorageConfig logStorageConfig;

    @Resource
    private LogStorageService logStorageService;

    @PostMapping("/upload")
    public Result uploadLogs(@RequestBody List<LogInfoDTO> logInfoDTOS){
        try {
            Preconditions.checkNotNull(logInfoDTOS,"日志参数为空");
        }catch (Exception e){
            throw new BaseException("参数为空");
        }
        log.info("LogStorageController.upload.logInfoDTOS:{}", JSON.toJSONString(logInfoDTOS));
        logStorageService.uploadLogs(logInfoDTOS);
        return Result.ok();
    }

    @GetMapping("/query")
    public Result<LogInfoVO> queryLogs(@RequestBody LogQueryDTO logQueryDTO){
        try{
            Preconditions.checkNotNull(logQueryDTO.getHostname(),"机器名称不能为空");
            Preconditions.checkNotNull(logQueryDTO.getFile(),"文件路径不能为空");
        }catch (Exception e){
            throw new BaseException("参数为空");
        }
        log.info("LogStorageController.query.logQueryDTO:{}", JSON.toJSONString(logQueryDTO));
        LogInfoVO logInfoVO = logStorageService.queryLogs(logQueryDTO);
        log.info("LogStorageController.query.logInfoVO:{}", JSON.toJSONString(logInfoVO));
        return Result.ok(logInfoVO);
    }

    @GetMapping("/testnacos")
    public String testnacos(){
        return logStorageConfig.getLogStorage();
    }
}
