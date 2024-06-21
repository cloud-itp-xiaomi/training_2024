package com.lx.server.mapper;

import com.lx.server.pojo.LogMessage;
import com.lx.server.pojo.LogMysql;
import com.lx.server.pojo.Utilization;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LogMapper {
    //插入日志
    @Insert("insert into log (hostName, file, log) VALUES " + "(#{hostName}, #{file}, #{log})")
    int insert(LogMysql log);

    //根据主机名和日志文件名查询日志
    @Select("select * from log where hostName = #{hostName} and file = #{file}")
    List<LogMysql> query(String hostName, String file);
}
