package com.example.springcloud.service;

import com.example.springcloud.CollectorApplication;
import com.example.springcloud.base.SystemInfoUtils;
import com.example.springcloud.controller.CollectorController;
import com.example.springcloud.controller.request.CollectorRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @ClassName BaseTest
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-06 01:28
 **/
@SpringBootTest(classes = {CollectorApplication.class})
@RunWith(SpringRunner.class)
public class BaseTest {
    @Autowired
    private CollectorService collectorService;
    @Autowired
    private CollectorController collectorController;
    @Test
    public void test() {
        System.out.println("test");
    }
    @Test
    public void testSendCollector(){
        CollectorRequest request = new CollectorRequest();
        request.setMetric("cpu.used.percent");
        request.setEndpoint("测试");
        request.setTimestamp(1717596227L);
        request.setStep(60);
        request.setValue(60.10f);
        collectorService.sendCollector(request);
    }

    @Test
    public void testCpuMsg(){
        SystemInfoUtils systemInfoUtils = new SystemInfoUtils();
        float cpuInfo = systemInfoUtils.getCpuInfo();
        System.out.println(cpuInfo);
        float osInfo = systemInfoUtils.getOsInfo();
        System.out.println(osInfo);

    }
    @Test
    public void testMemMsg(){
        SystemInfoUtils systemInfoUtils = new SystemInfoUtils();
        Float memInfo = systemInfoUtils.getMemInfo();
        System.out.println(memInfo);
    }
    @Test
    public void testEndpoint() throws UnknownHostException {
        String hostName =  InetAddress.getLocalHost().getHostName();
        System.out.println(hostName);
        SystemInfoUtils systemInfoUtils = new SystemInfoUtils();
        String host = systemInfoUtils.getHostName();
        System.out.println(host);
        long l = System.currentTimeMillis() / 1000;
        System.out.println(l);
    }
    @Test
    public void testCollectMsg(){
        collectorController.sendTask();
    }

}
