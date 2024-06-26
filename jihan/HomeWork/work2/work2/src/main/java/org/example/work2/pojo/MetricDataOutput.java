package org.example.work2.pojo;

import lombok.Data;

import java.util.List;
@Data
public class MetricDataOutput {
    private String metric;
    private List<ValueEntry> values;
}
