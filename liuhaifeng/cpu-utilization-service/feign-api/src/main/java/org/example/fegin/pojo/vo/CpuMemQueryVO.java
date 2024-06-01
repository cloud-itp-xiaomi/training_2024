package org.example.fegin.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author liuhaifeng
 * @date 2024/05/30/16:45
 */
@Data
public class CpuMemQueryVO implements Serializable {

    private String metric;

    private List<Value> values;

    @Data
    public static class Value implements Serializable{

        private Long timestamp;

        private Double value;

    }
}
