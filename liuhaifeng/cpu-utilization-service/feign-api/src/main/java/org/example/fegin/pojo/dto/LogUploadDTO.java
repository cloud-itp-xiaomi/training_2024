package org.example.fegin.pojo.dto;

import lombok.Data;

import java.util.List;

/**
 * 上传日志接口请求参数
 *
 * @author liuhaifeng
 * @date 2024/06/05/16:31
 */
@Data
public class LogUploadDTO {

    /**
     * 当前主机名称
     */
    private String hostname;

    /**
     * ⽇志⽂件的全路径
     */
    private String file;

    /**
     * ⽇志⽂件内容，每个元素是⼀条⽇志
     */
    private List<String> logs;
}
