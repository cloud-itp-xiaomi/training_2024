package com.h_s.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
@Repository
public interface LogEntryLogsRepository extends JpaRepository<LogEntryLogs, Long> {
    List<LogEntryLogs> findByLogEntryId(Long logEntryId);
}
