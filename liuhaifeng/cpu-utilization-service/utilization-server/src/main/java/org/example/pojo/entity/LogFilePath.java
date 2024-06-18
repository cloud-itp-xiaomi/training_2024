package org.example.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 日志文件路径表
 *
 * @author liuhaifeng
 * @date 2024/06/13/17:24
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogFilePath {

    /**
     * 主键
     */
    private Integer id;

    /**
     * ⽇志⽂件的全路径
     */
    private String filePath;

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
