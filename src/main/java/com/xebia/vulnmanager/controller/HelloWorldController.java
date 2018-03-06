package com.xebia.vulnmanager.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.logging.Logger;

@Controller
@EnableAutoConfiguration
@RequestMapping(value = {""})
public class HelloWorldController {
    private static final Logger LOGGER = Logger.getLogger("HelloWorldController");

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
    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    @ResponseBody
    // With PathVariable you can get a specific variable in this case id
    String test(@PathVariable("id") int id) {
        return "The chosen id is:" + id;
    }
}
