package org.example.work2.pojo;

import lombok.Data;

import java.util.List;

@Data
public class Cpu {
    private int id;
    private String metric;
    private String endpoint;
    private String timestamp;
    private String step;
    private String value;

    private List<ValueEntry> values;

}
