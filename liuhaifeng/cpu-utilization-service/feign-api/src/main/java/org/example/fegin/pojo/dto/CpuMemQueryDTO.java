package org.example.fegin.pojo.dto;

import lombok.Data;

import java.sql.Timestamp;

/**
 * 查询接口请求参数
 *
 * @author liuhaifeng
 * @date 2024/05/30/16:37
 */
@Data
public class CpuMemQueryDTO {

    private String endpoint;

    private String metric;

    private Long startTs;

    private Long endTs;

}
