package com.cl.server.controller;

import com.alibaba.fastjson.JSON;
import com.cl.server.convert.StatusConverter;
import com.cl.server.entity.Status;
import com.cl.server.pojo.DTO.StatusDTO;
import com.cl.server.pojo.DTO.StatusQueryDTO;
import com.cl.server.pojo.VO.StatusResp;
import com.cl.server.entity.Result;
import com.cl.server.exception.BaseException;
import com.cl.server.service.StatusService;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
/**
 * (Status)表控制层
 *
 * @author tressures
 * @date  2024-05-26 17:05:56
 */
@Slf4j
@RestController
@RequestMapping("/api/metric")
public class StatusController {

    @Resource
    private StatusService statusService;

    @PostMapping("/upload")
    public Result uploadMetric(@RequestBody List<StatusDTO> statusDTOS){
        try {
            Preconditions.checkNotNull(statusDTOS,"获取指标为空");
        }catch (Exception e){
            throw new BaseException(e.getMessage());
        }
        log.info("CpuStatusController.upload.cpuStatusList:{}", JSON.toJSONString(statusDTOS));
        List<Status> statusList = StatusConverter.INSTANCE.convertDTOListToEntityList(statusDTOS);
        statusService.uploadMetrics(statusList);
        return Result.ok();
    }

    @GetMapping("/query")
    public Result<List<StatusResp>> queryMetrics(@RequestBody StatusQueryDTO statusQueryDTO){
        try{
            Preconditions.checkNotNull(statusQueryDTO.getEndpoint(),"参数为空");
            Preconditions.checkNotNull(statusQueryDTO.getStart_ts(),"参数为空");
            Preconditions.checkNotNull(statusQueryDTO.getEnd_ts(),"参数为空");
        }catch (Exception e){
            throw new BaseException(e.getMessage());
        }
        if(statusQueryDTO.getStart_ts()>=statusQueryDTO.getEnd_ts()){
            throw new BaseException("参数异常");
        }
        log.info("CpuStatusController.query.statusQueryDTO:{}", JSON.toJSONString(statusQueryDTO));
        List<StatusResp> list = statusService.queryMetrics(statusQueryDTO);
        return Result.ok(list);
    }
}

