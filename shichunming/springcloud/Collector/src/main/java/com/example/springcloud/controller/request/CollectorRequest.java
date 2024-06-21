package com.example.springcloud.controller.request;

import lombok.Data;

import java.util.List;

/**
 * @ClassName CollectorRequest
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-04 20:14
 **/
@Data
public class CollectorRequest {
    /**
     * 采集数据类型
     */
    private String metric;
    /**
     * 主机名
     */
    private String endpoint;
    /**
     * 采集时间
     */
    private Long timestamp;
    /**
     * 采集间隔
     */
    private Integer step;
    /**
     * 采集值
     */
    private Float value;
    /**
     * 标签
     */
    private List<TagRequest> tags;

}
