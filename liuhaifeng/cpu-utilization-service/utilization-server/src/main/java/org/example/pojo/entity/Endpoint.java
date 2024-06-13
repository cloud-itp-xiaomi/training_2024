package org.example.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 主机表
 *
 * @author liuhaifeng
 * @date 2024/05/29/17:42
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Endpoint {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 主机名称
     */
    private String name;

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
