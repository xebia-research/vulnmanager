package com.xebia.vulnmanager;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@EnableAutoConfiguration
public class HelloWorldController {
    @RequestMapping("/ping")
    @ResponseBody
    String home() {

        return "pong";
    }

    @RequestMapping("/")
    @ResponseBody
    String test()
    {
        return "Hello world";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(HelloWorldController.class, args);
    }
}
