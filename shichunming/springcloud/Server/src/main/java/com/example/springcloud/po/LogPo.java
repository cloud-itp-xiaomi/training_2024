package com.example.springcloud.po;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName logPo
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-23 01:51
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class LogPo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String hostname;
    private String file;
    private String log;
    private Integer isDelete;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
