package com.winter.feign;

import com.winter.req.UploadReq;
import com.winter.resp.CommonResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "server", url = "http://localhost:8008/server")
public interface ServerFeign {
    @GetMapping("/hello")
    public String hello();

    @PostMapping("/metric/upload")
    public CommonResp add(UploadReq[] uploadReqs);
}
