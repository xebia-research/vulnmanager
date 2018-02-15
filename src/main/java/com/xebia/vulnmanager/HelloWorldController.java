package com.xebia.vulnmanager;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

@Controller
@EnableAutoConfiguration
public class HelloWorldController {
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    String home() {
        return "This is the home screen. You can go to an id, and you can also go to ping.";
    }

    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    @ResponseBody
    String ping() {
        return "pong";
    }

    // {} is to get a integer as an id in this case
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    // With PathVariable you can get a specific variable in this case id
    String test(@PathVariable("id") int id) throws IOException {
        return "The chosen id is:"+id;
    }



    public static void main(String[] args) throws Exception {
        SpringApplication.run(HelloWorldController.class, args);
    }
}
