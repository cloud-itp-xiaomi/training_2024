package com.jiuth.sysmonitorcapture.mapper;

import com.jiuth.sysmonitorcapture.dao.LogEntry;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author jiuth
 */
@Mapper
public interface LogMapper {

    @Insert("INSERT INTO logs (hostname, file, log,timestamp) VALUES (#{hostname}, #{file}, #{log},now())")
    void insertLog(LogEntry logEntry);
}