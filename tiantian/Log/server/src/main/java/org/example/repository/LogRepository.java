package org.example.repository;

import org.example.entity.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

public interface LogRepository extends JpaRepository<LogEntry, Long> {

  List<LogEntry> findByHostnameAndFile(String hostname, String file);
}
