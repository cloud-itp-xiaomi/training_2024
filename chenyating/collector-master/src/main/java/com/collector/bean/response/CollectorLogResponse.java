package com.collector.bean.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class CollectorLogResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String hostname;

    private String file;

    private String[] logs;
}
