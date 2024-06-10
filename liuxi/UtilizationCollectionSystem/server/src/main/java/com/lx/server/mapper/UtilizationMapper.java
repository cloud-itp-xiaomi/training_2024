package com.lx.server.mapper;

import com.lx.server.pojo.Utilization;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface UtilizationMapper {
    //插入主机利用率
    @Insert("insert into utilization (endpoint , metric , timestamp , step , value) VALUES " + "(#{endpoint},#{metric},#{timestamp},#{step},#{value})")
    int insert(Utilization utilization);

    //根据相应指标查询主机利用率
    @Select("select * from utilization where endpoint = #{endpoint} and metric = #{metric} and timestamp between #{start_ts} and #{end_ts}")
    List<Utilization> queryByMetric(String endpoint , String metric , Long start_ts , Long end_ts);

    //查询主机利用率
    @Select("select * from utilization where endpoint = #{endpoint}  and timestamp between #{start_ts} and #{end_ts}")
    List<Utilization> query(String endpoint , Long start_ts , Long end_ts);
}
