package com.hw.server.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hw.server.domain.dto.LogsDTO;
import com.hw.server.domain.dto.Result;
import com.hw.server.domain.po.Logs;
import com.hw.server.mapper.LogsMapper;
import com.hw.server.service.ILogsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mrk
 * @since 2024-06-04
 */
@Service
public class MysqlLogsServiceImpl extends ServiceImpl<LogsMapper, Logs> implements ILogsService {

    @Override
    @Transactional
    public Result<?> uploadLogs(List<Logs> logs) {
        try {
            this.saveBatch(logs);
            return Result.ok();
        } catch (Exception e) {
            return Result.error("Logs upload failed");
        }
    }

    @Override
    public Result<?> queryLogs(String hostname, String file) {
        List<Logs> logsList = this.list(new LambdaQueryWrapper<Logs>()
                .eq(Logs::getHostname, hostname)
                .eq(Logs::getFile, file));
        if (CollectionUtil.isEmpty(logsList)) {
            return Result.error("Data not exist");
        }

        List<String> logContents = new ArrayList<>();
        for (Logs logs : logsList) {
            if (StrUtil.isNotBlank(logs.getLog())) {
                logContents.add(logs.getLog());
            }
        }

        LogsDTO logsDTO = new LogsDTO();
        logsDTO.setHostname(hostname);
        logsDTO.setFile(file);
        logsDTO.setLogContents(logContents);

        return Result.ok(logsDTO);
    }
}
