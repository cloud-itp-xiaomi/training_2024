package org.example.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 日志表
 *
 * @author liuhaifeng
 * @date 2024/06/09/18:30
 */
@AllArgsConstructor
@Builder
@Data
public class Log {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 主机id
     */
    private Integer endpointId;

    /**
     * 日志文件路径表id
     */
    private Integer filePathId;

    /**
     * ⽇志⽂件的内容
     */
    private String logContent;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     */
    private Integer deleted;
}
