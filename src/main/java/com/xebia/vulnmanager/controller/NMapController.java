package com.xebia.vulnmanager.controller;

import com.xebia.vulnmanager.auth.AuthenticationChecker;
import com.xebia.vulnmanager.models.net.ErrorMsg;
import com.xebia.vulnmanager.models.nmap.objects.NMapReport;
import com.xebia.vulnmanager.util.ReportUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

@Controller
@RequestMapping(value = "/{company}/{team}/nmap")
public class NMapController {
    private final Logger LOGGER = LoggerFactory.getLogger("NMapController");

    private NMapReport getNMapReportFromObject(Object parsedDocument) throws ClassCastException {
        if (!(parsedDocument instanceof NMapReport)) {
            throw new ClassCastException("Object was not of type NMapReport");
        }
        return (NMapReport) parsedDocument;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getNMapReport(@RequestHeader(value = "auth", defaultValue = "nope") String authKey,
                                    @PathVariable("company") String companyName,
                                    @PathVariable("team") String teamName) {
        AuthenticationChecker authenticationChecker = new AuthenticationChecker();
        if (!authenticationChecker.checkTeamAndCompany(companyName, authKey, teamName)) {
            return new ResponseEntity(new ErrorMsg("Auth not correct!"), HttpStatus.BAD_REQUEST);
        }

        Object parsedDocument = ReportUtil.parseDocument(ReportUtil.getDocumentFromFile(new File("example_logs/nmap.xml")));
        NMapReport report;
        try {
            report = getNMapReportFromObject(parsedDocument);
        } catch (ClassCastException exception) {
            LOGGER.error(exception.getMessage());
            return new ResponseEntity(new ErrorMsg("The file requested is not of the right type"), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(report, HttpStatus.OK);
    }
}
