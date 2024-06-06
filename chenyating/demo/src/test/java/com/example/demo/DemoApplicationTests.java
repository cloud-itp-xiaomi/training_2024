package com.example.demo;

import com.example.demo.bean.User;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
class DemoApplicationTests {
    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
    }

    @Test
    @Transactional
    @Rollback(true)
    public void test_addUser() {
        User user = new User("小王", 100);
        userService.addUser(user);

        List<User> list = userService.getUsers();
        for (User u : list) {
            System.out.println(u);
        }
    }

    @Test
    @Transactional
    @Rollback(true)
    public void test_getUsers() {
        List<User> list = userService.getUsers();
        for (User user : list) {
            System.out.println(user);
        }
    }
    @Test
    @Transactional
    @Rollback(true)
    public void test_getUsersWithoutId() {
        List<User> list = userService.getUsersWithoutId();
        for (User user : list) {
            System.out.println(user);
        }
    }
    @Test
    @Transactional
    @Rollback(true)
    public void test_getUserById() {
        User user = userService.getUserById(1L);
        if (user == null) {
            throw new IllegalArgumentException("User with ID 1 does not exist.");
        }
        System.out.println(user);
    }
    @Test
    @Transactional
    @Rollback
    public void test_updateUser() {
        User user = userService.getUserById(1L);
        if (user == null) {
            throw new IllegalArgumentException("User with ID 1 does not exist.");
        }
        user.setName("小丽");
        user.setAge(100);
        userService.updateUser(user);
        test_getUsers();
    }
    @Test
    @Transactional
    @Rollback
    public void test_deleteUserById() {
        userService.deleteUserById(2L);
        test_getUsers();
    }
}
