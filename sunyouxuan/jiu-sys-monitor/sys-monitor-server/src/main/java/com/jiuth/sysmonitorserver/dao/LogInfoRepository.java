package com.jiuth.sysmonitorserver.dao;

import com.jiuth.sysmonitorserver.dao.enity.LogInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogInfoRepository extends JpaRepository<LogInfo, Long> {
    List<LogInfo> findByHostnameAndFile(String hostname, String file);
}