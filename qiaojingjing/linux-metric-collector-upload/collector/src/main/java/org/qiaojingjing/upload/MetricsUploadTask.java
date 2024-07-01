package org.qiaojingjing.upload;

import lombok.extern.slf4j.Slf4j;
import org.qiaojingjing.collector.CollectCpu;
import org.qiaojingjing.collector.CollectMem;
import org.qiaojingjing.entity.Metric;
import org.qiaojingjing.utils.POSTRequestUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 6.11由传统java项目改为springboot项目
 * 定时上传 step: 1min
 * 定时上传 1s
 * @author qiaojingjing
 * @version 0.1.0
 * @since 0.1.0
 **/
@Slf4j
@Component
public class MetricsUploadTask {
    @Resource
    private CollectCpu collectCpu;
    @Resource
    private CollectMem collectMem;
    private final Metric[] metrics = new Metric[2];

    //@Scheduled(fixedRate = 1000)
    public void collectMetrics() {
        try {
            metrics[0] = collectCpu.collect();
            metrics[1] = collectMem.collect();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    //@Scheduled(fixedRate = 60000, initialDelay = 5000)
    public void uploadMetrics() {
        long timestamp = System.currentTimeMillis() / 1000;
        metrics[0].setTimestamp(timestamp);
        metrics[1].setTimestamp(timestamp);
        POSTRequestUtil.sendMetricsPostRequest(metrics);
    }

    
}
