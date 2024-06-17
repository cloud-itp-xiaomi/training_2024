package com.example.mi1.mapper;

import com.example.mi1.domain.UploadParam;
import com.example.mi1.domain.po.Log;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LogMapper {
    @Select("SELECT * FROM log WHERE hostname = #{hostname} AND file = #{file}")
    List<Log> getAllLogs(String hostname, String file);

    @Insert("INSERT INTO log(hostname, file, log, timestamp) " + "VALUES (#{hostname}, #{file}, #{log}, #{timestamp})")
    void insertLog(Log log);
}
