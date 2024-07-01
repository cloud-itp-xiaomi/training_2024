package com.collector.bean.request;

import lombok.Data;

@Data
public class LogQueryRequest {
    // @NotBlank(message = "当前主机名称不能为空")
    private String hostname;

    // @NotBlank(message = "日志文件的全路径不能为空")
    private String file;
}
