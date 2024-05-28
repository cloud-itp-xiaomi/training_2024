package com.xiaomi.work1.collector;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.management.OperatingSystemMXBean;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;

/**
 * ClassName: CPUInfo
 * Package: com.xiaomi.work1.collector
 * Description:
 *
 * @Author WangYang
 * @Create 2024/5/26 16:08
 * @Version 1.0
 */
public class CpuInfo {

    /**
     * 返回当前主机的CPU使用率
     * 读取/proc/stat文件，读取cpu空闲时间idle和cpu总时间cpuTotal
     * 取样两次
     * cpu利用率=1-（idle2-idle1)/(cpu2-cpu1)
     * @return
     */
    public double getcpuUse() {

        try (BufferedReader reader = new BufferedReader(new FileReader("/proc/stat"))){
            String line = null;
            while((line=reader.readLine())!=null){
                if(line.startsWith("cpu")){
                    String[] res = line.trim().split("\\s+");
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
}
