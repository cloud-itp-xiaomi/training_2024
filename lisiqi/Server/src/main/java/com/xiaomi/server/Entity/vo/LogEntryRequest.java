package com.xiaomi.server.Entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class LogEntryRequest {
    private String hostname;
    private String file;
    private List<String> logs;
}
