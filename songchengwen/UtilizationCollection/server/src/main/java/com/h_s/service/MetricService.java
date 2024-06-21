package com.h_s.service;

import com.h_s.entity.Metric;

import java.util.List;

public interface MetricService {
    void saveMetrics(List<Metric> metrics);

    List<Metric> getMetrics(String endpoint, String metric, long start_ts, long end_ts);

}
