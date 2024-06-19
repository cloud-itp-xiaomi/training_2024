package com.xiaomi.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaomi.server.Entity.LogEntry;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogEntryMapper extends BaseMapper<LogEntry> {
}
