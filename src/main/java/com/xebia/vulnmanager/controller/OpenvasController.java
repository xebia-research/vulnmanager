package com.xebia.vulnmanager.controller;


import com.xebia.vulnmanager.openvas.objects.OpenvasReport;
import com.xebia.vulnmanager.util.ReportUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping(value = "/openvas")
public class OpenvasController {
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
        // With PathVariable you can get a specific variable in this case id
    ResponseEntity<?> test() throws IOException {
        return new ResponseEntity<OpenvasReport>(ReportUtil.parseDocument(ReportUtil.getDocumentFromFile(new File("example_logs/openvas.xml"))), HttpStatus.OK);
    }
}
