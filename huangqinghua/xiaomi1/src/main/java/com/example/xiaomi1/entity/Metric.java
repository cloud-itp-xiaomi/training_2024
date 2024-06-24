package com.example.xiaomi1.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@TableName(value = "collection")
public class Metric {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String metric;
    private String endpoint;
    private long timestamp;
    private Integer step;
    private double value;

    @Override
    public String toString() {
        return "Metric{" +
                "id='" + id + '\'' +
                ", metric='" + metric + '\'' +
                ", endpoint='" + endpoint+'\''+
                ", timestamp='" + timestamp+'\''+
                ", step='" + step + '\''+
                ", value='" + value + '\''+
                '}';
    }
}
