package com.example.demo.dao;

import com.example.demo.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
// CrudRepository 内置了一些的CRUD方法（右图）：
// userRepository.findAll();
// userRepository.findById(id);
// boolean existsById(id);
// userRepository.save(user);
// userRepository.deleteById(id);
// userRepository.delete(user);
// userRepository.deleteAll();
}