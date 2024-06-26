package com.cl.server.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.io.Serializable;
/**
 * (CpuStatus)实体类
 *
 * @author makejava
 * @since 2024-06-12 18:14:30
 */
@Data
public class Status implements Serializable {
    private static final long serialVersionUID = 914187712430699574L;
    
    private Integer id;
    
    private String metric;
    
    private String endpoint;
    
    private Long timestamp;
    
    private Long step;
    
    private Double value;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    private Integer isDelete;
}

