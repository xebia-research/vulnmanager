package com.xebia.vulnmanager.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/nmap")
public class NmapController {

    @RequestMapping("/")
    public String index() {
        return "Nmap controller";
    }
}
