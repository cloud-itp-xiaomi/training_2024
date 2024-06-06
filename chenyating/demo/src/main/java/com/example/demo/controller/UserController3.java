package com.example.demo.controller;

import com.example.demo.bean.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController3{
    @Autowired
    private UserService userService;
    //处理"/user/"的GET请求，用来获取User列表
    @GetMapping("/")
    public List<User> getUserList() {
        List<User> list = userService.getUsers();
        return list;
    }
    //处理"/user/{id}"的GET请求，用来获取url中id值的User信息
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }
    //处理"/user/"的POST请求，用来创建User
    @PostMapping("/")
    public User addUser(@RequestBody User user) throws Exception{
        Long id = user.getId();
        if( userService.getUserById(id)!=null ) {
            throw new Exception("id重复，添加失败"); //抛出异常
        }
        userService.addUser(user);
        return user;
    }
    //处理"/user/"的PUT请求，用来更新User信息 注：id值一般不做修改
    @PutMapping("/")
    public boolean updateUser(@RequestBody User user) throws Exception {
        Long id = user.getId();
        if ( userService.getUserById(id)==null ) {
            throw new Exception("id不存在，更新失败");
        }
        userService.updateUser(user);
        return true;
    }
    //处理"/user/{id}"的DELETE请求，用来删除User
    @DeleteMapping("/{id}")
    public boolean deleteUser( @PathVariable Long id ) throws Exception {
        if ( userService.getUserById(id)==null ) {
            throw new Exception("id不存在，删除失败");
        }
        userService.deleteUserById(id);
        return true;
    }
} //end controller