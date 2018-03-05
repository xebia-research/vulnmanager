package com.xebia.vulnmanager.controller;


import com.xebia.vulnmanager.openvas.objects.OpenvasReport;
import com.xebia.vulnmanager.openvas.objects.OvResult;
import com.xebia.vulnmanager.util.ReportUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
    ResponseEntity<?> getReport() throws IOException {
        return new ResponseEntity<OpenvasReport>(ReportUtil.parseDocument(ReportUtil.getDocumentFromFile(new File("example_logs/openvas.xml"))), HttpStatus.OK);
    }

    @RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
    @ResponseBody
        // With PathVariable you can get a specific variable in this case id
    ResponseEntity<?> getResult(@PathVariable("id") int id) throws IOException {
        OpenvasReport report = ReportUtil.parseDocument(ReportUtil.getDocumentFromFile(new File("example_logs/openvas.xml")));

        if (id >= report.getResults().size() || id <= 0) {
            return new ResponseEntity<String>("Error", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<OvResult>(report.getResults().get(id), HttpStatus.OK);
        }
    }
}
