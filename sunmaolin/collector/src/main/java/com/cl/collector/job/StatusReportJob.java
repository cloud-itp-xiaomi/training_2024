package com.cl.collector.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cl.collector.service.ReportService;
import com.cl.collector.service.StatusService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

/**
 * 定时采集任务类
 *
 * @author: tressures
 * @date: 2024/5/26
 */
@Component
@Slf4j
public class StatusReportJob {

    private static final String TO_SERVER_URL = "http://117.72.68.247:8888/api/metric/upload";

    @Resource
    private StatusService statusService;

    @Resource
    private ReportService reportService;

    @XxlJob("collectAndReportJobHandler")
    public void collectAndReportJobHandler() {
        XxlJobHelper.log("collectAndReportJobHandler.run");
        try{
            InetAddress localHost = InetAddress.getLocalHost();
            String hostName = localHost.getHostName();
            Long timeStamp = System.currentTimeMillis()/1000;
            Double cpuUsage = statusService.getCpuUsage();
            Double memUsage = statusService.getMemUsage();
            JSONArray status = new JSONArray();

            JSONObject cpuStatue = new JSONObject();
            cpuStatue.put("metric", "cpu.used.percent");
            cpuStatue.put("endpoint", hostName);
            cpuStatue.put("timestamp", timeStamp);
            cpuStatue.put("step", 60L);
            cpuStatue.put("value", cpuUsage);

            JSONObject memStatue = new JSONObject();
            memStatue.put("metric", "mem.used.percent");
            memStatue.put("endpoint", hostName);
            memStatue.put("timestamp", timeStamp);
            memStatue.put("step", 60L);
            memStatue.put("value", memUsage);

            status.add(cpuStatue);
            status.add(memStatue);
            reportService.report(status,TO_SERVER_URL);
        }catch (Exception e){
            XxlJobHelper.log("collectAndReportJobHandler.error:"+e.getMessage());
        }
    }
}
