package com.xebia.vulnmanager.controller;

import com.xebia.vulnmanager.auth.AuthenticationChecker;
import com.xebia.vulnmanager.models.net.ErrorMsg;
import com.xebia.vulnmanager.models.nmap.objects.NMapReport;
import com.xebia.vulnmanager.util.ReportUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

@RestController
@RequestMapping(value = "/{company}/{team}/nmap")
public class NMapController {
    private final Logger logger = LoggerFactory.getLogger("NMapController");

    @ModelAttribute("isAuthenticated")
    boolean setAuthenticateBoolean(@RequestHeader(value = "auth", defaultValue = "nope") String authKey,
                                   @PathVariable("company") String companyName,
                                   @PathVariable("team") String teamName) {
        AuthenticationChecker authenticationChecker = new AuthenticationChecker();
        return authenticationChecker.checkTeamAndCompany(companyName, authKey, teamName);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getNMapReport(@ModelAttribute("isAuthenticated") boolean isAuthenticated) {
        if (!isAuthenticated) {
            return new ResponseEntity(new ErrorMsg("Auth not correct!"), HttpStatus.BAD_REQUEST);
        }

        Object parsedDocument = ReportUtil.parseDocument(ReportUtil.getDocumentFromFile(new File("example_logs/nmap.xml")));
        NMapReport report = ReportUtil.getNMapReportFromObject(parsedDocument);
        if (report == null) {
            return new ResponseEntity<>(new ErrorMsg("The file requested is not of the right type"), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(report, HttpStatus.OK);
    }
}
