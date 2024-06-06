package com.example.demo.mapper;

import com.example.demo.bean.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper2 {
    User getOne(@Param("id") Long id);
    void insert(User user);
    public List<User> getAll();
    public List<User> getAllWithoutId();
    void update(User user);
    void delete(@Param("id") Long id);

}