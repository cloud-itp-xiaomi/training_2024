package org.qiaojingjing.service.Impl;

import org.qiaojingjing.constant.ExceptionConstant;
import org.qiaojingjing.exception.MyIllegalParamException;
import org.qiaojingjing.handler.storage.StorageTypeHandler;
import org.qiaojingjing.handler.storage.StorageTypeHandlerFactory;
import org.qiaojingjing.pojo.dto.HostDTO;
import org.qiaojingjing.pojo.dto.LogDTO;
import org.qiaojingjing.pojo.vo.LogVO;
import org.qiaojingjing.service.LogService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    @Override
    public void saveLogs(LogDTO[] logs) {
        List<LogDTO> logList = new ArrayList<>(Arrays.asList(logs));
        StorageTypeHandler storageHandler = StorageTypeHandlerFactory.getStorageHandler();
        storageHandler.save(logList);
    }

    @Override
    public List<LogVO> queryLogs(HostDTO hostDTO) {
        StorageTypeHandler storageHandler = StorageTypeHandlerFactory.getStorageHandler();
        if(hostDTO.getHostname()==null
                || hostDTO.getFile()==null){
            throw new MyIllegalParamException(ExceptionConstant.ILLEGAL_PARAM);
        }
        List<LogVO> logsList = storageHandler.query(hostDTO);

        return logsList;
    }
}
