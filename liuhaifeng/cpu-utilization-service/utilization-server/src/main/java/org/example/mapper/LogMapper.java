package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.pojo.entity.Log;

import java.util.List;

/**
 * @author liuhaifeng
 * @date 2024/06/09/18:27
 */
@Mapper
public interface LogMapper {

    void insertBatch(@Param("logList") List<Log> logList);

    List<Log> query(@Param("endpointId") Integer endpointId,
                    @Param("filePathId") Integer filePathId,
                    @Param("deleted") Integer deleted);
}
