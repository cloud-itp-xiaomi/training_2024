package com.cl.server.entity;

import lombok.Data;

import java.io.Serializable;


@Data
public class CpuStatus implements Serializable {
    private static final long serialVersionUID = 271764723178638140L;
    
    private Integer id;
    
    private String metric;
    
    private String endpoint;
    
    private Long timestamp;
    
    private Long step;
    
    private Double value;

}

