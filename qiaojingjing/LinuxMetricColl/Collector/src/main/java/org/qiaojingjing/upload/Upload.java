package org.qiaojingjing.upload;

import org.qiaojingjing.collector.CollectCpu;
import org.qiaojingjing.collector.CollectMem;
import org.qiaojingjing.entity.Metric;
import org.qiaojingjing.utils.POSTRequest;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 定时采集上传 step: 1min
 * @version 0.1.0
 * @author qiaojingjing
 * @since 0.1.0
 **/
public class Upload {
    private Timer timer = new Timer();
    private Metric[] metrics = new Metric[2];

    public Upload(int interval){
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    metrics[0] = CollectMem.collect();
                    metrics[1] = CollectCpu.collect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                metrics[0].setTimestamp(System.currentTimeMillis());
                metrics[1].setTimestamp(System.currentTimeMillis());
                POSTRequest.sendPostRequest(metrics);
                //System.out.println("运行了");
            }
        };

        timer.schedule(timerTask,0,interval);

    }


    public static void main(String[] args) {
        new Upload(60000); //60s
    }

}
