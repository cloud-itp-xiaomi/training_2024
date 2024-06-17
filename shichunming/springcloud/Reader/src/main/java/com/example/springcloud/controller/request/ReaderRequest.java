package com.example.springcloud.controller.request;

import lombok.Data;

/**
 * @ClassName ReaderRequest
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-06 18:42
 **/
@Data
public class ReaderRequest {
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
