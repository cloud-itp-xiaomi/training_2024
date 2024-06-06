package com.example.demo.service;

import com.example.demo.bean.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.mapper.UserMapper2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@CacheConfig( cacheNames = "user" )
public class UserService implements IUserService {
//    @Autowired
//    private UserMapper userMapper;
    @Autowired
    private UserMapper2 userMapper;
    @Override
    @Cacheable( key = "#id", condition="#id!=null", unless="#result == null" )
    public User getUserById(Long id) {
        return userMapper.getOne(id);
    }
    @Override
    @CachePut( key = "'all_users'", unless="#result == null" )
    public List<User> getUsers() {
        return userMapper.getAll();
    }
    @Override
    public List<User> getUsersWithoutId() {
        return userMapper.getAllWithoutId();
    }
    @Override
    public void addUser(User user) {
        userMapper.insert(user);
    }
    @Override
    @CachePut( key="#user.id" )
    public User updateUser(User user) {
        userMapper.update(user);
        return user;
    }
    @Override
    @CacheEvict(key="#id")
    public void deleteUserById(Long id) {
        userMapper.delete(id);
    }
}