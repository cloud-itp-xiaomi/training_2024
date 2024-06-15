package com.collector.sync;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.collector.forest.MyClient;
import com.sun.management.OperatingSystemMXBean;

@Component
public class Collector {

	@Autowired
	private MyClient client;
	
	@Scheduled(cron="0/1 * *  * * ? ")   //每1秒执行一次
    public void onApplicationReady() {
    	try {
    		InetAddress addr = InetAddress.getLocalHost();
    		
    		String hostName = addr.getHostName();
    		
    		OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
            
    		// List listReq = new ArrayList();
			List<Map<String, Object>> listReq = new ArrayList<>();
            // 获取CPU使用率
            double cpuLoad = osBean.getSystemCpuLoad() * 100;
            if (Double.isNaN(cpuLoad)) {
                System.out.println("Unable to obtain CPU load");
            } else {
            	String str = String.format("%.2f",cpuLoad);
            	cpuLoad = Double.parseDouble(str);
            	// Map reqMap = new HashMap();
				Map<String, Object> reqMap = new HashMap<>();
        		reqMap.put("endpoint", hostName);
            	reqMap.put("metric", "cpu.used.percent");
            	reqMap.put("timestamp", new Date().getTime()/1000);
            	reqMap.put("value", cpuLoad);
            	listReq.add(reqMap);
            }
            
            // 获取总物理内存与可用内存
            long totalMemory = osBean.getTotalPhysicalMemorySize();
            long freeMemory = osBean.getFreePhysicalMemorySize();
            long usedMemory = totalMemory - freeMemory;
            // 计算内存使用率
            double memoryUsageRate = (double) usedMemory / totalMemory * 100;
            String str = String.format("%.2f",memoryUsageRate);
			// Map reqMap = new HashMap();
			Map<String, Object> reqMap = new HashMap<>();
    		reqMap.put("endpoint", hostName);
            memoryUsageRate = Double.parseDouble(str);
            reqMap.put("metric", "mem.used.percent");
            reqMap.put("timestamp", new Date().getTime()/1000);
        	reqMap.put("value", memoryUsageRate);
        	listReq.add(reqMap);
        	client.dataUpload(listReq);
			Thread.sleep(1000);
		} catch (InterruptedException | UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
