package com.xebia.vulnmanager.controller;

import com.xebia.vulnmanager.auth.AuthenticationChecker;
import com.xebia.vulnmanager.models.clair.objects.ClairReport;
import com.xebia.vulnmanager.models.net.ErrorMsg;
import com.xebia.vulnmanager.services.ClairService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/{company}/{team}/clair")
public class ClairController {
    private final Logger logger = LoggerFactory.getLogger("ClairController");

    private AuthenticationChecker authenticationChecker;

    private ClairService clairService;

    @Autowired
    public ClairController(final ClairService clairService,
                           final AuthenticationChecker authenticationChecker) {
        this.clairService = clairService;
        this.authenticationChecker = authenticationChecker;
    }

    /**
     * Get all Clair reports
     *
     * @return Http response to get call
     * @throws IOException An exception if a report is not found
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getAllZapReports() throws IOException {
        List<ClairReport> reportList = clairService.getAllReports();
        return new ResponseEntity<>(reportList, HttpStatus.OK);
    }

    /**
     * Get a parsed test report of Clair
     *
     * @return A response with correct http header
     */
    @RequestMapping(value = "{reportId}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getReportById(@PathVariable("reportId") long reportId) {
        ClairReport report = clairService.getReportById(reportId);
        if (report != null) {
            return new ResponseEntity<>(report, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMsg("report not found"), HttpStatus.NOT_FOUND);
        }
    }
}
