package com.example.hostmonitor.controller;

import com.example.hostmonitor.pojo.QueryData;
import com.example.hostmonitor.pojo.Result;
import com.example.hostmonitor.pojo.UploadData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Author: WangYF
 * @Date: 2024/05/27
 * @Description: 主机利用率采集接口
 */

@Controller
@RequestMapping("api/metric")
public class MonitorController {

    /**
     * @Author: WangYF
     * @Date: 2024/05/27
     * @Description: 上报接口
     */
    @PostMapping("upload")
    public Result upload(List<UploadData> uploadData){
        return Result.success();
    }


    /**
     * @Author: WangYF
     * @Date: 2024/05/27
     * @Description: 查询
     */
    @GetMapping("query")
    public Result query(QueryData queryData){
        return Result.success();
    }
}
