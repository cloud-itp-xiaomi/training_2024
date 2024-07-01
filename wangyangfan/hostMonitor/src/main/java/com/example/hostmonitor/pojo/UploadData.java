package com.example.hostmonitor.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author: WangYF
 * @Date: 2024/05/28
 * @Description: 主机利用率采集接口输入
 */
@Data
@AllArgsConstructor
public class UploadData {
    private String metric;
    private String endpoint;
    private Long timestamp;
    private Long step;
    private double value;

}
