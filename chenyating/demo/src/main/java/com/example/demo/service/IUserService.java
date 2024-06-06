package com.example.demo.service;

import com.example.demo.bean.User;

import java.util.List;

public interface IUserService {
    public User getUserById(Long id);
    public List<User> getUsers();
    public List<User> getUsersWithoutId();
    public void addUser(User user);
    public User updateUser(User user);
    public void deleteUserById(Long id);
}