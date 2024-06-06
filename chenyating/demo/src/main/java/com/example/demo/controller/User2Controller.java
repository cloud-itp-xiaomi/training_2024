package com.example.demo.controller;

import com.example.demo.bean.User;
import com.example.demo.result.Result;
import com.example.demo.result.ResultCode;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v2/user")
public class User2Controller {
    static Map<Long, User> users = new HashMap<>();

    // url: localhost:8080/v1/user/ GET请求，获取User列表
    @GetMapping("/")
    public Result<List<User>> getUserList() {
        List<User> list = new ArrayList<>( users.values() );
        return Result.success(list);
    }

    // url: localhost:8080/v1/user/2020001 GET请求，获取url中id值对应的User信息
    @GetMapping("/{id}")
    public Result<User> getUser( @PathVariable("id") Long id ) {
        return Result.success(users.get(id));
    }

    // url: localhost:8080/v1/user/ POST请求，用来创建User
    @PostMapping("/")
    public Result addUser( @RequestBody User user ) throws Exception {
        Long id = user.getId();
        if( id == null ) {
            return Result.fail(ResultCode.ID_NOT_FOUND);
        }
        if(users.containsKey(id)) {
            return Result.fail(ResultCode.ID_DUPLICATED);
        }
        users.put(id, user);
        return Result.success(user);
    }

    // url: localhost:8080/v1/user/ PUT请求，用来更新User信息 注：id值一般不做修改
    @PutMapping("/")
    public Result updateUser( @RequestBody User user ) throws Exception {
        Long id = user.getId();
        if (!users.containsKey(id)) {
            return Result.fail(ResultCode.ID_NOT_FOUND);
        }
        User u = users.get(id); //根据id查找对象
        u.setName(user.getName());
        u.setAge(user.getAge());
        users.put(id, u); //写入
        return Result.success(true);
    }

    // url: localhost:8080/v1/user/2020001 DELETE请求，用来删除User
    @DeleteMapping("/{id}")
    public Result deleteUser( @PathVariable Long id ) throws Exception {
        if (!users.containsKey(id)) {
            return Result.fail(ResultCode.ID_NOT_FOUND);
        }
        users.remove(id);
        return Result.success(true);
    }

}