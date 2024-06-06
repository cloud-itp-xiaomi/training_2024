package com.example.demo.controller;

import com.example.demo.bean.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/user")
public class UserController {
    static Map<Long, User> users = new HashMap<>();

    // url: localhost:8080/v1/user/ GET请求，获取User列表
    @GetMapping("/")
    public List<User> getUserList() {
        List<User> list = new ArrayList<>( users.values() );
        return list;
    }

    // url: localhost:8080/v1/user/2020001 GET请求，获取url中id值对应的User信息
    @GetMapping("/{id}")
    public User getUser( @PathVariable("id") Long id ) {
        return users.get(id);
    }

    // url: localhost:8080/v1/user/ POST请求，用来创建User
    @PostMapping("/")
    public User addUser( @RequestBody User user ) throws Exception {
        Long id = user.getId();
        if( id == null ) {
            throw new Exception("id为空，添加失败"); //抛出异常
        }
        if(users.containsKey(id)) {
            throw new Exception("id重复，添加失败"); //抛出异常
        }
        users.put(id, user);
        return user;
    }

    // url: localhost:8080/v1/user/ PUT请求，用来更新User信息 注：id值一般不做修改
    @PutMapping("/")
    public boolean updateUser( @RequestBody User user ) throws Exception {
        Long id = user.getId();
        if (!users.containsKey(id)) {
            throw new Exception("id不存在，更新失败");
        }
        User u = users.get(id); //根据id查找对象
        u.setName(user.getName());
        u.setAge(user.getAge());
        users.put(id, u); //写入
        return true;
    }

    // url: localhost:8080/v1/user/2020001 DELETE请求，用来删除User
    @DeleteMapping("/{id}")
    public boolean deleteUser( @PathVariable Long id ) throws Exception {
        if (!users.containsKey(id)) {
            throw new Exception("id不存在，删除失败");
        }
        users.remove(id);
        return true;
    }

}