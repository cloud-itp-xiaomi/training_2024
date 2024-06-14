package com.example.mi1.domain;

import lombok.Data;

@Data
public class UploadParam {
    private String metric;
    private String endpoint;
    private Long timestamp;
    private Long step;
    private float value;
}
