package com.xebia.vulnmanager;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NmapController {

    @RequestMapping("/")
    public String index() {
        return "";
    }
}
