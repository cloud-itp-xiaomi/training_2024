package com.cl.collector.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cl.collector.service.StatusCollectorService;
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

@Component
@Slf4j
public class StatusReportJob {
    private static final String TO_SERVER_URL = "http://117.72.68.247:8888/api/metric/upload";
    @Resource
    private StatusCollectorService statusCollectorService;

    @XxlJob("collectAndReportJobHandler")
    public void collectAndReportJobHandler() {
        XxlJobHelper.log("collectAndReportJobHandler.run");
        try{
            InetAddress localHost = InetAddress.getLocalHost();
            String hostName = localHost.getHostName();
            Long timeStamp = System.currentTimeMillis()/1000;
            Double cpuUsage = statusCollectorService.getCpuUsage();
            Double memUsage = statusCollectorService.getMemUsage();
            JSONArray status = new JSONArray();

            JSONObject cpuStatue = new JSONObject();
            cpuStatue.put("metric", "cpu.used.percent");
            cpuStatue.put("endpoint", hostName);
            cpuStatue.put("timestamp", timeStamp);
            cpuStatue.put("step", 60);
            cpuStatue.put("value", cpuUsage);

            JSONObject memStatue = new JSONObject();
            memStatue.put("metric", "mem.used.percent");
            memStatue.put("endpoint", hostName);
            memStatue.put("timestamp", timeStamp);
            memStatue.put("step", 60);
            memStatue.put("value", memUsage);

            status.add(cpuStatue);
            status.add(memStatue);
            report(status);
            System.out.println("cpuStatue: "+cpuStatue+" memStatue: "+memStatue);
        }catch (Exception e){
            XxlJobHelper.log("collectAndReportJobHandler.error:"+e.getMessage());
        }
    }


    public  void report(JSONArray jsonArray) throws IOException{

        URL url = new URL(TO_SERVER_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setDoOutput(true);
        try (OutputStream os = conn.getOutputStream()) {
            byte[] inputBytes = jsonArray.toString().getBytes("utf-8");
            os.write(inputBytes, 0, inputBytes.length);
            os.flush();
        }
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println("Response: " + response.toString());
            }
        } else {
            System.out.println("Error: " + responseCode+";Message: "+conn.getResponseMessage());
        }
    }
}
