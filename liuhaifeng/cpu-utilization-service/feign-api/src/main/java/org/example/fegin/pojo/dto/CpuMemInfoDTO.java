package org.example.fegin.pojo.dto;

import lombok.Data;



/**
 * 上传数据接口请求参数
 *
 * @author liuhaifeng
 * @date 2024/05/30/0:04
 */
@Data
public class CpuMemInfoDTO {

    private String metric;

    private String endpoint;

    private Long timestamp;

    private Integer step;

    private Double value;

    private String tags;
}
