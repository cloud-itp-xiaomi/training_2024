package com.example.demo01.mapper;

import com.example.demo01.bean.DataItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DataMapper {
    @Select("select * from table01")
    public List<DataItem> getAll();

    @Select("select * from table01 where id = #{id}")
    DataItem getFromId(@Param("id") int id);

    @Select("select metric, timestamp, value from table01" +
            "where metric = #{metric} and endpoint = #{endpoint}" +
            "and timestamp between #{start_ts} and #{end_ts}")
    @Results({
            @Result(property = "metric", column = "metric"),
            @Result(property = "timestamp", column = "timestamp"),
            @Result(property = "value", column = "value")
    })
    public List<DataItem> getAllBy(@Param("metric") String metric, @Param("endpoint") String endpoint,
                                   Integer start_ts,Integer end_ts);

    @Insert("insert into table01(metric, endpoint, timestamp, step, value) " +
            "values( #{metric}, #{endpoint}, #{timestamp}, #{step}, #{value})")
    void insert(DataItem item);

    @Delete("delete from table01 where id =#{id}")
    void delete(@Param("id") int id);
}
