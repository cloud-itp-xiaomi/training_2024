package com.example.demo01.controller;

import com.example.demo01.bean.DataItem;
import com.example.demo01.result.Result;
import com.example.demo01.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/metric")
public class ServerController {
    @Autowired
    private DataService dataService;

    @GetMapping("/query")
    public List<DataItem> getDataList(@RequestParam(value = "metric") String metric,
                                                 @RequestParam(value = "endpoint") String endpoint,
                                                 @RequestParam(value = "start_ts") Integer start_ts,
                                                 @RequestParam(value = "end_ts") Integer end_ts) {
        return dataService.getAllData(metric, endpoint, start_ts, end_ts);
    }
}
