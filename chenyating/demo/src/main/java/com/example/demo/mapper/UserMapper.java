package com.example.demo.mapper;

import com.example.demo.bean.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from user where id = #{id}")
    User getOne(@Param("id") Long id);

    @Insert("insert into user(name,age) values( #{name}, #{age} )")
    void insert(User user);

    @Select("select * from user")
    public List<User> getAll();

    @Select("select name, age from user") //只选了两个字段
    @Results({
            @Result(property = "name", column = "name"),
            @Result(property = "age", column = "age")
    })
    public List<User> getAllWithoutId();

    @Update("update user set age=#{age},name=#{name} where id =#{id}")
    void update(User user);

    @Delete("delete from user where id =#{id}")
    void delete(@Param("id") Long id);

}