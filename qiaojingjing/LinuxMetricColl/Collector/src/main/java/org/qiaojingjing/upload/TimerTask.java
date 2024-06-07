package org.qiaojingjing.upload;

import org.qiaojingjing.collector.CollectCpu;
import org.qiaojingjing.collector.CollectMem;
import org.qiaojingjing.constants.Param;
import org.qiaojingjing.entity.Metric;
import org.qiaojingjing.utils.POSTRequest;

import java.io.IOException;
import java.util.Timer;

/**
 * 定时上传 step: 1min
 *
 * @author qiaojingjing
 * @version 0.1.0
 * @since 0.1.0
 **/
public class TimerTask {
    private Timer timer = new Timer();
    private Metric[] metrics = new Metric[2];

    public TimerTask(int interval ) {
        java.util.TimerTask collectorTimer = new java.util.TimerTask() {
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
        timer.schedule(collectorTimer,
                 0,
                1000);

        java.util.TimerTask uploadTimer = new java.util.TimerTask() {
            @Override
            public void run() {
                long uploadTimeMills = System.currentTimeMillis();
                metrics[0].setTimestamp(uploadTimeMills);
                metrics[1].setTimestamp(uploadTimeMills);
                POSTRequest.sendPostRequest(metrics);
                System.out.println("上传数据");
            }
        };
        timer.schedule(uploadTimer,
                 5000,
                       interval);
    }


    public static void main(String[] args) {
        new TimerTask(Param.INTERVAL);
    }

}
