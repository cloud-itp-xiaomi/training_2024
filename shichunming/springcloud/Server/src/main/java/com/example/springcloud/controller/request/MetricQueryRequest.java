package com.example.springcloud.controller.request;

import lombok.Data;

/**
 * @ClassName MetricQueryRequest
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-05 12:23
 **/
@Data
public class MetricQueryRequest {

    /*
     * 主机名
     */
    private String endpoint;
    /*
     * 数据类型
     */
    private String metric;
    /*
     * 开始时间戳
     */
    private Long start_ts;
    /*
     * 结束时间戳
     */
    private Long end_ts;
}
