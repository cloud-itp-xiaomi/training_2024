package com.example.hostcollector.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author: WangYF
 * @Date: 2024/05/29
 * @Description: 发送给请求的JSON封装Bean，同为主机利用率采集接口输入
 */

@Data
@AllArgsConstructor
public class UploadData {
    private String metric;
    private String endpoint;
    private Long timestamp;
    private Long step;
    private Double value;
}
