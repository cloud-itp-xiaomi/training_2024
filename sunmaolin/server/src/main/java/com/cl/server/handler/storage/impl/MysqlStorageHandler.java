package com.cl.server.handler.storage.impl;

import com.cl.server.entity.LogAddress;
import com.cl.server.entity.LogInfo;
import com.cl.server.enums.IsDeletedFlagEnum;
import com.cl.server.exception.BaseException;
import com.cl.server.mapper.LogAddressDao;
import com.cl.server.mapper.LogInfoDao;
import com.cl.server.pojo.DTO.LogInfoDTO;
import com.cl.server.pojo.DTO.LogQueryDTO;
import com.cl.server.pojo.VO.LogInfoVO;
import com.cl.server.enums.StorageTypeEnum;
import com.cl.server.handler.storage.StorageTypeHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MYSQL存储方式
 *
 * @author: tressures
 * @date: 2024/6/5
 */
@Component
@Slf4j
public class MysqlStorageHandler implements StorageTypeHandler {

    @Resource
    private LogAddressDao logAddressDao;

    @Resource
    private LogInfoDao logInfoDao;

    @Override
    public StorageTypeEnum getHandlerType() {
        return StorageTypeEnum.MYSQL;
    }

    @Override
    public void upload(List<LogInfoDTO> logInfoDTOS) {
        for(LogInfoDTO logInfoDTO : logInfoDTOS){
            LogAddress logAddress = new LogAddress();
            logAddress.setHostName(logInfoDTO.getHostname());
            logAddress.setFile(logInfoDTO.getFile());
            Integer addressId = logAddressDao.queryByLimit(logAddress);
            //保证主机名+日志路径-->唯一性
            if(addressId == null){
                logAddress.setIsDelete(IsDeletedFlagEnum.UN_DELETED.getCode());
                logAddress.setCreateTime(LocalDateTime.now());
                logAddressDao.insert(logAddress);
                //执行完插入语句后，自动将自增id赋值给对象的属性id
                addressId = logAddress.getId();
            }
            //日志和log_address表的绑定
            for(String log : logInfoDTO.getLogs()) {
                LogInfo logInfo = new LogInfo();
                logInfo.setLogAddressId(addressId);
                logInfo.setInfo(log);
                logInfo.setCreateTime(LocalDateTime.now());
                logInfo.setIsDelete(IsDeletedFlagEnum.UN_DELETED.getCode());
                logInfoDao.insert(logInfo);
            }
        }
    }

    @Override
    public LogInfoVO query(LogQueryDTO logQueryDTO) {
        LogInfoVO logInfoVO = new LogInfoVO();
        logInfoVO.setHostname(logQueryDTO.getHostname());
        logInfoVO.setFile(logQueryDTO.getFile());
        LogAddress logAddress = new LogAddress();
        logAddress.setHostName(logQueryDTO.getHostname());
        logAddress.setFile(logQueryDTO.getFile());
        logAddress.setIsDelete(IsDeletedFlagEnum.UN_DELETED.getCode());
        Integer addressId = logAddressDao.queryByLimit(logAddress);
        if(addressId == null){
            throw new BaseException("主机或日志路径不存在");
        }
        LogInfo logInfo = new LogInfo();
        logInfo.setLogAddressId(addressId);
        List<LogInfo> logInfos = logInfoDao.queryAllByLimit(logInfo);
        if(CollectionUtils.isEmpty(logInfos)){
            return logInfoVO;
        }
        //按日志插入时间大小降序排序
        List<String> logs = logInfos.stream()
                .sorted(Comparator.comparing(LogInfo::getCreateTime).reversed())
                .map(LogInfo::getInfo)
                .collect(Collectors.toList());
        logInfoVO.setLogs(logs);
        return logInfoVO;
    }
}
