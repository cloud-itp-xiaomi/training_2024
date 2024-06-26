package com.cl.server.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;
/**
 * (LogAddress)实体类
 *
 * @author makejava
 * @since 2024-06-06 17:27:33
 */
@Data
public class LogAddress implements Serializable {
    private static final long serialVersionUID = 571802231946182152L;
    
    private Integer id;
    /**
     * 主机名称
     */
    private String hostName;
    /**
     * 日志文件全路径
     */
    private String file;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 是否删除
     */
    private Integer isDelete;

}

