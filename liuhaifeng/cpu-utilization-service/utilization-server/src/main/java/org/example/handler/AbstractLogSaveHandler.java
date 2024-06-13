package org.example.handler;

import org.example.pojo.entity.Log;

import java.util.List;

/**
 * 日志保存处理器
 *
 * @author liuhaifeng
 * @date 2024/06/09/17:24
 */
public interface AbstractLogSaveHandler {

    /**
     * 日志上传
     *
     * @param logsList
     */
    void upload(List<Log> logsList);

    /**
     * 日志查询
     *
     * @param endpointId
     * @param filePathId
     * @param deleted
     * @return
     */
    List<Log> query(Integer endpointId, Integer filePathId, Integer deleted);
}
