package com.cl.collector.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cl.collector.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * 上报服务
 *
 * @author: tressures
 * @date: 2024/6/14
 */
@Service("reportService")
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Override
    public void report(JSONArray jsonArray, String toServerUrl)  {
        log.info("ReportServiceImpl.report.jsonArray:{}", JSON.toJSONString(jsonArray));
        try {
            URL url = new URL(toServerUrl);
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
                    log.info("ReportServiceImpl.report.response:{}", JSON.toJSONString(response));
                }
            } else {
                log.info("ReportServiceImpl.report.response.error.message:{}", conn.getResponseMessage());
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
