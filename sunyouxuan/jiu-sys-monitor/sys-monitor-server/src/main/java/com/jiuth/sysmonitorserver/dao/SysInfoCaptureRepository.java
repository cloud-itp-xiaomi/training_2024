package com.jiuth.sysmonitorserver.dao;

import com.jiuth.sysmonitorserver.dao.enity.SysInfoCapture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysInfoCaptureRepository extends JpaRepository<SysInfoCapture, Long> {
//    List<SysInfoCapture> findByEmail(String email);

    List<SysInfoCapture> findByEndpointAndMetricAndTimestampBetween(String endpoint, String metric, Long startTs, Long endTs);
}
