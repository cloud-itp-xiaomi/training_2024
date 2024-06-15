package org.qiaojingjing.handler.storage;

import org.qiaojingjing.pojo.dto.HostDTO;
import org.qiaojingjing.pojo.dto.LogDTO;
import org.qiaojingjing.pojo.entity.Log;
import org.qiaojingjing.pojo.vo.LogVO;

import java.util.List;

public interface StorageTypeHandler {
    void save(List<LogDTO> logList);

    List<LogVO> query(HostDTO hostDTO);
}
