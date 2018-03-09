package com.xebia.vulnmanager.controller;

import com.xebia.vulnmanager.auth.AuthenticationChecker;
import com.xebia.vulnmanager.models.net.ErrorMsg;
import com.xebia.vulnmanager.models.openvas.objects.OpenvasReport;
import com.xebia.vulnmanager.util.ReportUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping(value = "/{company}/{team}/openvas")
public class OpenvasController {

    private final Logger LOGGER = LoggerFactory.getLogger("OpenvasController");

    private OpenvasReport getOpenvasReportFromObject(Object parsedDocument) throws ClassCastException {
        if (!(parsedDocument instanceof OpenvasReport)) {
            throw new ClassCastException("Object was not of type OpenvasReport");
        }
        return (OpenvasReport) parsedDocument;
    }

    /**
     * Get a parsed test report of openvas
     *
     * @return A response with correct http header
     * @throws IOException An exception if the example log isn't found
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getReport(@RequestHeader(value = "auth", defaultValue = "nope") String authKey,
                                @PathVariable("company") String companyName,
                                @PathVariable("team") String teamName) throws IOException {
        AuthenticationChecker authenticationChecker = new AuthenticationChecker();
        if (!authenticationChecker.checkTeamAndCompany(companyName, authKey, teamName)) {
            return new ResponseEntity(new ErrorMsg("Auth not correct!"), HttpStatus.BAD_REQUEST);
        }

        Object parsedDocument = ReportUtil.parseDocument(ReportUtil.getDocumentFromFile(new File("example_logs/openvas.xml")));
        OpenvasReport report;
        try {
            report = getOpenvasReportFromObject(parsedDocument);
        } catch (ClassCastException exception) {
            LOGGER.error(exception.getMessage());
            return new ResponseEntity(new ErrorMsg("The file requested is not of the right type"), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(report, HttpStatus.OK);
    }

    /**
     * Get a certain result from a report.
     *
     * @param id The index id in the OpenvasReport.
     * @return A response with correct http header
     * @throws IOException Exception when example log isn't found or couldn't be opened
     */
    @RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getResult(@PathVariable("id") int id,
                                @RequestHeader(value = "auth", defaultValue = "nope") String authKey,
                                @PathVariable("company") String companyName,
                                @PathVariable("team") String teamName) throws IOException {
        AuthenticationChecker authenticationChecker = new AuthenticationChecker();
        if (!authenticationChecker.checkTeamAndCompany(companyName, authKey, teamName)) {
            return new ResponseEntity(new ErrorMsg("Auth not correct!"), HttpStatus.BAD_REQUEST);
        }

        Object parsedDocument = ReportUtil.parseDocument(ReportUtil.getDocumentFromFile(new File("example_logs/openvas.xml")));
        OpenvasReport report;
        try {
            report = getOpenvasReportFromObject(parsedDocument);
        } catch (ClassCastException exception) {
            LOGGER.error(exception.getMessage());
            return new ResponseEntity(new ErrorMsg("The file requested is not of the right type"), HttpStatus.BAD_REQUEST);
        }

        if (id >= report.getResults().size() || id < 0) {
            return new ResponseEntity<>(new ErrorMsg("Result not found"), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(report.getResults().get(id), HttpStatus.OK);
        }
    }
}
