package com.txh.xiaomi2024.work.collector.common;

public enum JobType {
    LOG("collector_work2"),
    UTILIZATION("collector_work1"),
    ;

    private String jobDetail;
    JobType(String jobDetail) {
        this.jobDetail = jobDetail;
    }

    public String getJobDetail() {
        return jobDetail;
    }
}
