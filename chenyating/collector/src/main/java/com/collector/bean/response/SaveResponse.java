package com.collector.bean.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SaveResponse implements Serializable {
    private static final long serialVersionUID = 3L;

    private List<String> files;

    private String logStorage;
}
