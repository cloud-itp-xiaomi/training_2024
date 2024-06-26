package org.example.work2.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.work2.pojo.Cpu;

import java.util.List;

@Mapper
public interface CupMapper {
    @Insert("insert into cpu(metric,endpoint,timestamp,step,value)" +
             "values(#{metric},#{endpoint},#{timestamp},#{step},#{value})")
    void add(Cpu cpu);

    @Select("select timestamp,value from cpu where metric = #{metric} and timestamp > #{startTs} and timestamp < #{endTs} and endpoint = #{endpoint}")
    List<Cpu> list(String metric, String endpoint, int startTs, int endTs);
}
