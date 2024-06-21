package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.pojo.entity.Endpoint;

/**
 * @author liuhaifeng
 * @date 2024/05/29/17:45
 */
@Mapper
public interface EndpointMapper {

    @Select("select * from endpoint where name = #{name}")
    Endpoint getEndpointByName(String name);

    void insert(Endpoint endpoint);
}
