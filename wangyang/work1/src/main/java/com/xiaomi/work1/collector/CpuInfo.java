package com.xiaomi.work1.collector;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.management.OperatingSystemMXBean;

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
     * @return
     */
    public double getcpuUse() {
        return 0;
    }
}
