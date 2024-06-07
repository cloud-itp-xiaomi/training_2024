package org.qiaojingjing.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
/**
 * 查询指标DTO
 * @version 0.1.0
 * @author qiaojingjing
 * @since 0.1.0
 **/

@Data
@AllArgsConstructor
public class MetricDTO {
    private String endpoint;
    private String metric;
    private Long startTs;
    private Long endTs;
}
