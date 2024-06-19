package com.txh.xiaomi2024.work;

public interface UtilizationUploadService {
    /**
     * collector上报server的数据
     * @param metric 指标名称
     * @param endpoint 当前主机名称
     * @param timestamp 数据采集时间
     * @param value 采集到的利用率的值
     * @return 提示上报成功与否的信息
     */
    String upload(String metric, String endpoint, int step, long timestamp, double value);
}
