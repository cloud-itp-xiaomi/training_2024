package com.cl.server.entity.DTO;

import lombok.Data;

@Data
public class StatusQueryDTO {
    private String endPoint;
    private String metric;
    private Long start_ts;
    private Long end_ts;
}
