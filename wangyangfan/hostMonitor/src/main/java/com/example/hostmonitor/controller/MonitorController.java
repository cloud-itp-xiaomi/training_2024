package com.example.hostmonitor.controller;

import com.example.hostmonitor.pojo.QueryData;
import com.example.hostmonitor.pojo.Result;
import com.example.hostmonitor.pojo.UploadData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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
    @ResponseBody
    @PostMapping("upload")
    public Result upload(@RequestBody ArrayList<UploadData> data){
        System.out.println(data);
        return Result.success();
    }


    /**
     * @Author: WangYF
     * @Date: 2024/05/27
     * @Description: 查询
     */
    @ResponseBody
    @GetMapping("query")
    public Result query(@RequestBody QueryData queryData){
        return Result.success();
    }

    @ResponseBody
    @GetMapping("hello")
    public Result query1(){
        return Result.success("hello world");
    }
}
