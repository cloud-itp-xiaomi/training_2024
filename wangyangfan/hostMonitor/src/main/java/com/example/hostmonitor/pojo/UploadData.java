package com.example.hostmonitor.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @Author: WangYF
 * @Date: 2024/05/28
 * @Description: 主机利用率采集接口输入
 */
@Component
@Data
public class UploadData {
    private String metric;
    private String endpoint;
    private Long timestamp;
    private Long step;
    private double value;

}
