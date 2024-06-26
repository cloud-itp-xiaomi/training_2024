package com.example.springcloud.mapper;

import com.example.springcloud.po.LogPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName XmLogMapper
 * @Description TODO
 * @Author 石心木PC
 * @create: 2024-06-23 02:12
 **/
public interface XmLogMapper {
    /**
     * 批量插入
     */
    int insertBatch(List<LogPo> poList);
    /**
     * 根据文件和主机名查询
     */
    List<LogPo> selectByFileAndHostname(@Param("file") String file, @Param("hostname") String hostname);
}
