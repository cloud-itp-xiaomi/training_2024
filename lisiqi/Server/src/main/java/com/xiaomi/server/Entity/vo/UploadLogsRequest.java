package com.xiaomi.server.Entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class UploadLogsRequest {
    private String logStorage;
    private List<LogEntryRequest> logs;
}
