package com.cl.server.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * (CpuStatus)实体类
 *
 * @author tressures
 * @date 2024-05-26 17:05:59
 */
@Data
public class CpuStatus implements Serializable {
    private static final long serialVersionUID = 271764723178635140L;
    
    private Integer id;
    
    private String metric;
    
    private String endpoint;
    
    private Long timestamp;
    
    private Long step;
    
    private Double value;


}

