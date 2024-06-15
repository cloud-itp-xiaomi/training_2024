package org.qiaojingjing.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.qiaojingjing.annotation.AutoFill;
import org.qiaojingjing.enums.OperationType;
import org.qiaojingjing.pojo.dto.HostDTO;
import org.qiaojingjing.pojo.entity.Log;
import org.qiaojingjing.pojo.vo.LogMysqlVO;
import org.qiaojingjing.pojo.vo.LogVO;

import java.util.List;

@Mapper
public interface LogMapper {

    @AutoFill(value = OperationType.INSERT)
    void insertLogs(@Param("logList") List<Log> logList);

    List<LogMysqlVO> queryLogsByHostnameAndFile(HostDTO hostDTO);
}
