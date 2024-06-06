package org.qiaojingjing.upload;

import org.qiaojingjing.collector.CollectCpu;
import org.qiaojingjing.collector.CollectMem;
import org.qiaojingjing.cons.Param;
import org.qiaojingjing.entity.Metric;
import org.qiaojingjing.utils.POSTRequest;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 定时上传 step: 1min
 * @version 0.1.0
 * @author qiaojingjing
 * @since 0.1.0
 **/
public class Upload {
    private Timer timer = new Timer();
    private Metric[] metrics = new Metric[2];

    public Upload(int interval) {
        // 定时采集
        TimerTask updateCpuTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    CollectCpu.getCurrentCpuValues();
                    metrics[0] = CollectMem.collect();
                    metrics[1] = CollectCpu.collect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(updateCpuTask, 0, 1000); //每秒采集一次

        // 定时上传
        TimerTask collectAndUploadTask = new TimerTask() {
            @Override
            public void run() {
                metrics[0].setTimestamp(System.currentTimeMillis());
                metrics[1].setTimestamp(System.currentTimeMillis());
                POSTRequest.sendPostRequest(metrics);
                System.out.println("上传数据");
            }
        };
        timer.schedule(collectAndUploadTask, 5000, interval); // 每分钟执行一次
    }


    public static void main(String[] args) {
        new Upload(Param.INTERVAL); //60s
    }

}
