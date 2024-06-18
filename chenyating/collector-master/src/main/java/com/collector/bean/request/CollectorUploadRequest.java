package com.collector.bean.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CollectorUploadRequest {

    @NotBlank(message = "当前主机名称不能为空")
    private String endpoint;

    @NotBlank(message = "指标名称不能为空")
    private String metric;

    @NotNull(message = "指标的采集周期不能为空")
    private Integer step;

    @NotNull(message = "采集到的 CPU 或内存利用率的值不能为空")
    private double value;

    @NotNull(message = "采集数据时的时间不能为空")
    private Integer timestamp;
}
