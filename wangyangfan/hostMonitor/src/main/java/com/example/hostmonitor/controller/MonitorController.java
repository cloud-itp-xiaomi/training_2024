package com.example.hostmonitor.controller;

import com.example.hostmonitor.pojo.QueryData;
import com.example.hostmonitor.pojo.QueryMsg;
import com.example.hostmonitor.pojo.Result;
import com.example.hostmonitor.pojo.UploadData;
import com.example.hostmonitor.service.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: WangYF
 * @Date: 2024/05/27
 * @Description: 主机利用率采集接口
 */

@Controller
@RequestMapping("api/metric")
public class MonitorController {

    @Autowired
    private MonitorService monitorService;

    /**
     * @Description: 上报接口
     */
    @ResponseBody
    @PostMapping("upload")
    public Result upload(@RequestBody ArrayList<UploadData> data){
        UploadData cpuData = data.get(0);
        UploadData memData = data.get(1);
        monitorService.saveAllData(memData, cpuData);
        return Result.success();
    }


    /**
     * @Description: 查询
     */
    @ResponseBody
    @GetMapping("query")
    public Result query(@ModelAttribute QueryMsg data){
        List<QueryData> returnData = monitorService.queryBetweenTime(data);
        return Result.success(returnData);
    }

    @ResponseBody
    @GetMapping("hello")
    public Result test(){
        return Result.success("hello world");
    }
}
