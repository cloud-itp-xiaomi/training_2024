package org.example.handler;

import org.example.pojo.entity.Logs;

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
    void upload(List<Logs> logsList);


    /**
     * 日志查询
     * @param endpointId
     * @param file
     * @param deleted
     * @return
     */
    List<Logs> query(Integer endpointId, String file, Integer deleted);


}
