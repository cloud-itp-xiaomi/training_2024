package org.example.fegin.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 查询日志接口返回VO
 *
 * @author liuhaifeng
 * @date 2024/06/05/16:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogQueryVO {

    /**
     * 主机名称
     */
    private String hostname;

    /**
     * 文件全路径
     */
    private String file;

    /**
     * 日志内容
     */
    private List<String> logs;
}
