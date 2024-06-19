package com.cl.server.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 状态查询DTO
 *
 * @author tressures
 * @date:  2024/5/27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusQueryDTO {

    private String endpoint;

    private String metric;

    private Long start_ts;

    private Long end_ts;
}
