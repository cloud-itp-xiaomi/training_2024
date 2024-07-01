package com.hw.server.service;

import com.hw.server.domain.dto.Result;
import com.hw.server.domain.po.Logs;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mrk
 * @since 2024-06-04
 */
public interface ILogsService extends IService<Logs> {

    Result<?> uploadLogs(List<Logs> logs);

    Result<?> queryLogs(String hostname, String file);
}
