package com.collector.bean.request;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CollectorQueryRequest {

    @NotBlank(message = "机器名称不能为空")
    private String endpoint;

    private String metric;

    @NotNull(message = "开始时间不能为空")
    private Integer start_ts;

    @NotNull(message = "结束时间不能为空")
    private Integer end_ts;
}
