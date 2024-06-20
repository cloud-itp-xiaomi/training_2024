package com.example.springbootdemo.demos.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 等同于@Controller加上@ResponseBody
 */
@RestController
public class controller {
    /**
     * 访问/hello或者/hi任何一个地址，都会返回同样的结果
     * @GetMapping等用于@RequestMapping(method = RequestMethod.GET)
     * @return
     */
    @GetMapping(value = {"/hello","/hi"})
    public String say() {
        return "How are you?";
    }
}
