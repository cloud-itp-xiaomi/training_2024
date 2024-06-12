package org.qiaojingjing.controller;

import lombok.extern.slf4j.Slf4j;
import org.qiaojingjing.pojo.dto.HostDTO;
import org.qiaojingjing.pojo.dto.LogDTO;
import org.qiaojingjing.pojo.vo.LogVO;
import org.qiaojingjing.result.Result;
import org.qiaojingjing.service.LogService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/log")
@Slf4j
public class LogController {
    @Resource
    private LogService logService;

    @PostMapping("/upload")
    public Result saveLogs(@RequestBody LogDTO[] logs){
        log.info("接收到上传的日志:{}", (Object) logs);
        logService.saveLogs(logs);
        return Result.success();
    }

    @GetMapping("query")
    public Result queryLogs(HostDTO hostDTO){
        log.info("接收查询{}的{}存储的日志",
                 hostDTO.getHostname(),
                 hostDTO.getFile());
        List<LogVO> logsList = logService.queryLogs(hostDTO);
        return Result.success(logsList);
    }

}
