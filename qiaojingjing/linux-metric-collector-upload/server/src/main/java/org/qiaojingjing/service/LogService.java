package org.qiaojingjing.service;

import org.qiaojingjing.pojo.dto.HostDTO;
import org.qiaojingjing.pojo.dto.LogDTO;
import org.qiaojingjing.pojo.entity.Log;
import org.qiaojingjing.pojo.vo.LogVO;

import java.util.List;

public interface LogService {
    void saveLogs(LogDTO[] logs);

    List<LogVO> queryLogs(HostDTO hostDTO);
}
