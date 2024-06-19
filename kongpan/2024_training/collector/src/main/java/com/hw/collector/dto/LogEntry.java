package com.hw.collector.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author mrk
 * @create 2024-06-04-19:55
 */
@Data
@TableName("logs")
public class LogEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 主机名称
     */
    private String hostname;

    /**
     * ⽇志⽂件的全路径
     */
    private String file;

    /**
     * ⽇志⽂件内容，每个元素是⼀条更新⽇志
     */
    private String log;
}
