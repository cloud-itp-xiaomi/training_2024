package com.jiuth.sysmonitorcapture.dao;

import lombok.Data;

import java.security.Timestamp;
import java.time.LocalDateTime;

/**
 * @author jiuth
 */
@Data
public class LogEntry {

    private Long id;

    private String hostname;


    private String file;

    private String log;

    //String timestamp;
    private LocalDateTime timestamp;

    // Getters and setters
}