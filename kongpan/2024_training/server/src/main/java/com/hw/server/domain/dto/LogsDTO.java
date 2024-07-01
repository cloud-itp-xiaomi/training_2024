package com.hw.server.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @author mrk
 * @create 2024-06-08-16:21
 */
@Data
public class LogsDTO {
    String hostname;
    String file;
    List<String> logContents;
}
