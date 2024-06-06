package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/aa/hello")
    public String hello1(){
        return "aa";
    }
    @GetMapping("/bb/hello")
    public String hello2(){
        return "bb";
    }
    @GetMapping("/cc/hello")
    public String hello3(){
        return "cc";
    }

    @PostMapping("/word")
    public String sensitive(String word) {
        return word;
    }

}