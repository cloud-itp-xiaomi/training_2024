package com.winter.controller;

import com.winter.feign.ServerFeign;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CollectorController {

    @Resource
    private ServerFeign serverFeign;

    /**
     * 主动查询接口
     *  * */
    @GetMapping("/queryCPU")
    public String queryCPU(){
        return serverFeign.hello();
    }
}
