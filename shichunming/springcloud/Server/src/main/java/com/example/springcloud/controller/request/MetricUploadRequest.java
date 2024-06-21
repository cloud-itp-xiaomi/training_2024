package com.example.springcloud.controller.request;

import lombok.Data;
import lombok.NonNull;

import java.util.List;

/**
 * @ClassName CollectorRequest
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-04 20:54
 **/
@Data
public class MetricUploadRequest {
    /**
     * 采集数据类型
     */
    @NonNull
    private String metric;
    /**
     * 主机名
     */
    @NonNull
    private String endpoint;
    /**
     * 采集时间
     */
    @NonNull
    private Long timestamp;
    /**
     * 采集间隔
     */
    @NonNull
    private Integer step;
    /**
     * 采集值
     */
    @NonNull
    private Float value;
    /**
     * 标签
     */
    private List<TagRequest> tags;

}
