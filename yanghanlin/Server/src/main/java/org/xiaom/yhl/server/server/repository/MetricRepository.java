package org.xiaom.yhl.server.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xiaom.yhl.server.server.entity.Metric;

import java.util.List;

/**
 * ClassName: MetricRepository
 * Package: org.xiaom.yhl.server.server.repository
 * Description:
 *
 * @Author 杨瀚林
 * @Create 2024/5/30 15:31
 * @Version 1.0
 */
public interface MetricRepository extends JpaRepository<Metric, Long> {
    List<Metric> findByEndpointAndMetricAndTimestampBetween(String endpoint, String metric, long startTs, long endTs);
}