package org.example.fegin.pojo;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author liuhaifeng
 * @date 2024/05/29/17:58
 */

@Data
public class CpuMem implements Serializable {

    private String metric;

    private String endpoint;

    private Timestamp timestamp;

    private Integer step;

    private Double value;

    private String tags;
}
