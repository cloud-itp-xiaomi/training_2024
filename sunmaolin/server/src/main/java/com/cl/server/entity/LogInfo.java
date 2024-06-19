package com.cl.server.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;
/**
 * (LogInfo)实体类
 *
 * @author makejava
 * @since 2024-06-06 17:28:13
 */
@Data
public class LogInfo implements Serializable {
    private static final long serialVersionUID = -64464090847679842L;
    
    private Integer id;
    /**
     * 外键，log_address表关联
     */
    private Integer logAddressId;
    /**
     * 日志内容
     */
    private String info;
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

