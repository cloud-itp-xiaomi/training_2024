package com.lx.server.service;

import cn.hutool.json.JSONUtil;
import com.lx.server.pojo.ResUtilization;
import com.lx.server.pojo.Utilization;
import org.junit.jupiter.api.Test;


class MessageConsumerTest {

    @Test
    void onMessage() {
        MessageConsumer messageConsumer = new MessageConsumer();
        ResUtilization resUtilization = new ResUtilization();
        Utilization utilization = new Utilization();
        utilization.setEndpoint("095dc3f39ca1");
        utilization.setMetric("mem.used.percent");
        utilization.setTimestamp(606013640L);
        utilization.setStep(60L);
        utilization.setValue(81.5);
        String strJson = JSONUtil.toJsonStr(resUtilization);
        messageConsumer.onMessage(strJson);
    }
}