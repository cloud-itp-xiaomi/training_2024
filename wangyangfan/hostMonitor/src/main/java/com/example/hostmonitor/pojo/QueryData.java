package com.example.hostmonitor.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class QueryData {
    private String endpoint;
    private String metric;
    private Long start_ts;
    private Long end_ts;
}
