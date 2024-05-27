package com.cl.server.entity;

import lombok.Data;

import java.util.List;

@Data
public class CpuStatusResp {

    private String metric;

    private List<Values> values;

}
