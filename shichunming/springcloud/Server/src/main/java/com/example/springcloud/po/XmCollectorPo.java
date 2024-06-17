package com.example.springcloud.po;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName XmCollectorInfoPo
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-04 23:44
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class XmCollectorPo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String endpoint;
    private String metric;
    private String tags;
    private Integer step;
    private Float value;
    private Long timestamp;
    private Integer isDelete;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}
