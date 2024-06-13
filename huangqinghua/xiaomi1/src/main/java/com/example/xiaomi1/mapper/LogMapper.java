package com.example.xiaomi1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.xiaomi1.entity.Log;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LogMapper extends BaseMapper<Log> {
    List<Log> getLog(
            @Param("hostname") String hostname,
            @Param("file") String file);
}
