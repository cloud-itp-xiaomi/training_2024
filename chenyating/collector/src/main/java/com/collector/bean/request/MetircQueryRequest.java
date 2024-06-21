package com.collector.bean.request;

import lombok.Data;

@Data
public class MetircQueryRequest {
    // @NotBlank(message = "机器名称不能为空")
    private String endpoint;

    private String metric;

    // @NotNull(message = "开始时间不能为空")
    private Integer start_ts;

    // @NotNull(message = "结束时间不能为空")
    private Integer end_ts;
}
