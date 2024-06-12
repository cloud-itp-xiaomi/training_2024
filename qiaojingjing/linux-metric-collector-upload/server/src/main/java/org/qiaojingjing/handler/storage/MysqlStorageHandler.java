package org.qiaojingjing.handler.storage;

import lombok.extern.slf4j.Slf4j;
import org.qiaojingjing.mapper.LogMapper;
import org.qiaojingjing.pojo.dto.HostDTO;
import org.qiaojingjing.pojo.dto.LogDTO;
import org.qiaojingjing.pojo.entity.Log;
import org.qiaojingjing.pojo.vo.LogMysqlVO;
import org.qiaojingjing.pojo.vo.LogVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MysqlStorageHandler implements StorageTypeHandler {

    @Resource
    private LogMapper logMapper;

    @Override
    @Transactional
    public void save(List<LogDTO> logDTOList) {
        log.info("使用mysql数据库存储");
        List<Log> logList = new ArrayList<>();
        for (LogDTO logDTO : logDTOList) {
            String[] logContents = logDTO.getLogs();
            for (String logContent : logContents) {
                Log log = Log.builder()
                             .hostname(logDTO.getHostname())
                             .file(logDTO.getFile())
                             .content(logContent)
                             .build();
                logList.add(log);
            }
        }
        logMapper.insertLogs(logList);
    }

    @Override
    public List<LogVO> query(HostDTO hostDTO) {
        log.info("在MySQL数据库中查询日志");
        List<LogVO> logVOList = new ArrayList<>();
        List<String> logContents = new ArrayList<>();
        List<LogMysqlVO> logMysqlVOS = logMapper.queryLogsByHostnameAndFile(hostDTO);
        for (LogMysqlVO logMysqlVO : logMysqlVOS) {
            logContents.add(logMysqlVO.getLogs());
        }
        LogVO logVO = LogVO.builder()
                .hostname(hostDTO.getHostname())
                .file(hostDTO.getFile())
                .logs(logContents)
                .build();
        logVOList.add(logVO);

        return logVOList;
    }
}
