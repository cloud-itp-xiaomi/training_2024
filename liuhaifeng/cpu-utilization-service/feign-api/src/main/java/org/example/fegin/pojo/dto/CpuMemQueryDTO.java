package org.example.fegin.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 查询cpu内存数据接口请求参数
 *
 * @author liuhaifeng
 * @date 2024/05/30/16:37
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CpuMemQueryDTO {

    /**
     * 主机名称
     */
    private String endpoint;

    /**
     * 查询的指标名称
     */
    private String metric;

    /**
     * 开始时间
     */
    private Long startTs;

    /**
     * 结束时间
     */
    private Long endTs;

}
