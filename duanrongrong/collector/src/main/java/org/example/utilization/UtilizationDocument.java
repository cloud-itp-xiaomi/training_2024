package org.example.utilization;

import lombok.Data;

@Data
public class UtilizationDocument {
    private String metric;
    private String endpoint;
    private Long timestamp;
    private Long step;
    private float value;
}
