package com.cl.collector.service;

import com.alibaba.fastjson.JSONArray;
import java.io.IOException;

/**
 * 上报接口
 *
 * @author: tressures
 * @date: 2024/6/14
 */
public interface ReportService {

    void report(JSONArray jsonArray, String toServerUrl) ;
}
