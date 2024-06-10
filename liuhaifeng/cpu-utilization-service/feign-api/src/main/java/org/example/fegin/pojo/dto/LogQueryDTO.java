package org.example.fegin.pojo.dto;

import lombok.Data;

/**
 * 查询日志接口请求参数
 *
 * @author liuhaifeng
 * @date 2024/06/05/16:31
 */
@Data
public class LogQueryDTO {

    /**
     * 查询的主机名称
     */
    private String hostname;

    /**
     * 查询的文件全路径
     */
    private String file;
}
