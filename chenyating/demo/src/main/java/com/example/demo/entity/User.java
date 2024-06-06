package com.example.demo.entity;

import lombok.Data;


import javax.persistence.*;
import java.io.Serializable;

@Entity // 声明类为实体类，不能掉！
@Data // Lombok注解
public class User {
    @Id //@Id注解：表示该属性作为表的主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) //主键生成策略：IDENTITY表示自增
    private Long id; // Long 对应MySQL数据库 bigint 类型
    @Column(nullable = false, unique = true, length = 20) //@Column注解设置列字段：非空唯一，最大长度20
    private String username;
    @Column(nullable = false, length = 20) //非空，最大长度20
    private String password;
}