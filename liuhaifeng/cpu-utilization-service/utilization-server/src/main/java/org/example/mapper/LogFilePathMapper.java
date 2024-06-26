package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.pojo.entity.LogFilePath;

/**
 * @author liuhaifeng
 * @date 2024/06/13/17:26
 */
@Mapper
public interface LogFilePathMapper {

    @Select("select * from log_file_path where file_path = #{filePath}")
    LogFilePath getLogFilePathByFilePath(String filePath);

    void insert(LogFilePath logFilePath);
}
