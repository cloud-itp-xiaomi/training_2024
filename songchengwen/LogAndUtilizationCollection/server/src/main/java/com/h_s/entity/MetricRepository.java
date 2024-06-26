package com.h_s.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetricRepository extends JpaRepository<Metric, Long> {
    List<Metric> findByEndpointAndTimestampBetween(String endpoint, long start_ts, long end_ts);

    List<Metric> findByEndpointAndMetricAndTimestampBetween(String endpoint, String metric, long startTs, long endTs);
}
