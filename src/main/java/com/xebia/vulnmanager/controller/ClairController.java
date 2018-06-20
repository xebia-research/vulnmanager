package com.xebia.vulnmanager.controller;

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
    private static final String COMPANY_LITTERAL = "company";
    private static final String TEAM_LITTERAL = "team";

    private final Logger logger = LoggerFactory.getLogger("ClairController");

    private ClairService clairService;

    @Autowired
    public ClairController(final ClairService clairService) {
        this.clairService = clairService;
    }

    /**
     * Get all Clair reports
     *
     * @return Http response to get call
     * @throws IOException An exception if a report is not found
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getAllClairReports(@PathVariable(COMPANY_LITTERAL) String companyName,
                                       @PathVariable(TEAM_LITTERAL) String teamName) throws IOException {
        List<ClairReport> reportList = clairService.getAllReportsByTeam(companyName, teamName);
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
