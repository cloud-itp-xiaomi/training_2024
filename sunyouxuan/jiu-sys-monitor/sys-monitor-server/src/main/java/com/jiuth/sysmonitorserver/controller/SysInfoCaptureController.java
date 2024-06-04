package com.jiuth.sysmonitorserver.controller;

import com.jiuth.sysmonitorserver.dao.enity.SysInfoCapture;
import com.jiuth.sysmonitorserver.dto.SysInfoCaptureDTO;
import com.jiuth.sysmonitorserver.service.SysInfoCaptureRedisService;
import com.jiuth.sysmonitorserver.service.SysInfoCaptureService;
import com.jiuth.sysmonitorserver.util.ApiResponse;
import com.jiuth.sysmonitorserver.util.converter.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/metric")
public class SysInfoCaptureController {

    @Autowired
    private SysInfoCaptureService sysInfoCaptureService;

    @Autowired
    private SysInfoCaptureRedisService sysInfoCaptureRedisService;

    @GetMapping
    public ApiResponse<List<SysInfoCapture>> getAllSysInfo() {
        try {
            List<SysInfoCapture> sysInfoCaptures = sysInfoCaptureService.findAll();
            return ApiResponse.success(sysInfoCaptures);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PostMapping("/upload")
    public ApiResponse<Void> createSysInfo(@RequestBody List<SysInfoCaptureDTO> sysInfoCaptureDTOs) {
        try {
            List<SysInfoCapture> sysInfoCaptures = Convert.toEntityList(sysInfoCaptureDTOs);
//            sysInfoCaptureService.saveAll(sysInfoCaptures);
//            return ResponseEntity.ok(ApiResponse.success(null));

            sysInfoCaptureService.saveAll(sysInfoCaptures);

            for (SysInfoCaptureDTO dto : sysInfoCaptureDTOs) {
                if ("cpu.used.percent".equals(dto.getMetric())) {
                    sysInfoCaptureRedisService.cacheCpuUsage(dto);
                } else if ("mem.used.percent".equals(dto.getMetric())) {
                    sysInfoCaptureRedisService.cacheMemUsage(dto);
                }
            }

            return ApiResponse.success(null);
        } catch (Exception e) {
            //TODO 能够返回redis错误信息
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/query")
    public ApiResponse<List<Map<String, Object>>> queryMetrics(
            @RequestParam String endpoint,
            @RequestParam(required = false) String metric,
            @RequestParam long start_ts,
            @RequestParam long end_ts
    ) {

        List<SysInfoCapture> captures = sysInfoCaptureService.query(endpoint, metric, start_ts, end_ts);
        Map<String, List<Map<String, Object>>> result = new HashMap<>();

        for (SysInfoCapture capture : captures) {
            String metricName = capture.getMetric();
            List<Map<String, Object>> values = result.getOrDefault(metricName, new ArrayList<>());

            Map<String, Object> data = new HashMap<>();
            data.put("timestamp", capture.getTimestamp());
            data.put("value", capture.getValue());

            values.add(data);
            result.put(metricName, values);
        }

        List<Map<String, Object>> response = new ArrayList<>();
        for (Map.Entry<String, List<Map<String, Object>>> entry : result.entrySet()) {
            Map<String, Object> metricData = new HashMap<>();
            metricData.put("metric", entry.getKey());
            metricData.put("values", entry.getValue());
            response.add(metricData);
        }

        return ApiResponse.success(response);
    }

    @GetMapping("/{id}")
    public ApiResponse<SysInfoCapture> getSysInfoById(@PathVariable Long id) {
        try {
            SysInfoCapture sysInfoCapture = sysInfoCaptureService.findById(id);
            if (sysInfoCapture != null) {
                return ApiResponse.success(sysInfoCapture);
            } else {
                return ApiResponse.error("Data not found");
            }
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSysInfo(@PathVariable Long id) {
        try {
            sysInfoCaptureService.deleteById(id);
            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}

