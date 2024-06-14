package com.example.demo01.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class MetricQueryForm {//查询参数

    @NotBlank(message = "主机名称不能为空")
    private String endpoint;

    public String metric;

    @JsonProperty(value = "start_ts")
    @NotNull(message = "开始时间不能为空")
    @Min(value = 0, message = "开始时间戳不能<0")
    private Long startTime;

    @JsonProperty(value = "end_ts")
    @NotNull(message = "截止时间不能为空")
    @Min(value = 0, message = "截止时间戳不能<0")
    private Long endTime;

}
