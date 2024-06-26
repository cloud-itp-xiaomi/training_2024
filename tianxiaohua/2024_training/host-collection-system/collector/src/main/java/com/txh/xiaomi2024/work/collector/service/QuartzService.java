package com.txh.xiaomi2024.work.collector.service;

public interface QuartzService {
    void startJob(String jobName, String jobGroup);
    void pauseJob(String jobName, String jobGroup);
    void resumeJob(String jobName, String jobGroup);
    void deleteJob(String jobName, String jobGroup);
}
