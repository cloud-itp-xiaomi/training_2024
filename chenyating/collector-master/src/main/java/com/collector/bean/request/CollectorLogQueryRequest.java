package com.collector.bean.request;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class CollectorLogQueryRequest {
    // @NotBlank(message = "当前主机名称不能为空")
    private String hostname;

    // @NotBlank(message = "日志文件的全路径不能为空")
    private String file;
}
