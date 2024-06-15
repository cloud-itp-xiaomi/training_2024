package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.pojo.entity.Logs;

import java.util.List;

/**
 * @author liuhaifeng
 * @date 2024/06/09/18:27
 */
@Mapper
public interface LogsMapper {

    void insertBatch(@Param("logsList") List<Logs> logsList);

    @Select("select * from logs where endpoint_id = #{endpointId} and file = #{file} and deleted = #{deleted}")
    List<Logs> query(@Param("endpointId") Integer endpointId,
                     @Param("file") String file,
                     @Param("deleted") Integer deleted);
}
