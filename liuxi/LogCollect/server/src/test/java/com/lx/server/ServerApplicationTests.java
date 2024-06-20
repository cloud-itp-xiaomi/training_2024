package com.lx.server;

import cn.hutool.json.JSONUtil;
import com.lx.server.pojo.ResUtilization;
import com.lx.server.pojo.Utilization;
import com.lx.server.service.MessageConsumeService;
import com.lx.server.utils.GetBeanUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.PostConstruct;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DependsOn(value = "getBeanUtil")
class ServerApplicationTests {

    @Autowired
    private GetBeanUtil getBeanUtil;

    private MessageConsumeService messageConsumer ;

    public ServerApplicationTests(){
    }

    /**
     * 初始化属性
     */
    @PostConstruct
    public void testGetBeanUtil() {
        assertNotNull(getBeanUtil);
        ApplicationContext applicationContext = GetBeanUtil.applicationContext;
        assertNotNull(GetBeanUtil.applicationContext);
        messageConsumer = getBeanUtil.getBean(MessageConsumeService.class);
    }

    @Test
    void onMessage() {
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
