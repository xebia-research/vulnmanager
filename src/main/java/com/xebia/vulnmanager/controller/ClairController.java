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
    private static final String IS_AUTHENTICATED_STRING = "isAuthenticated";
    private static final String AUTH_NOT_CORRECT_STRING = "Auth not correct!";

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
     * This function is called before other functions, so if for example getReport is called it first runs the init function
     *
     * @param authKey     Key of the user
     * @param companyName Name of the company of the user
     * @param teamName    Name of the team of the user
     * @return returns boolean, true if the company, team and authentication are correct
     */
    @ModelAttribute(IS_AUTHENTICATED_STRING)
    boolean setAuthenticateBoolean(@RequestHeader(value = "auth", defaultValue = "testauth") String authKey,
                                   @PathVariable("company") String companyName, @PathVariable("team") String teamName) {
        return authenticationChecker.checkTeamAndCompany(companyName, authKey, teamName);
    }

    /**
     * Get all Clair reports
     *
     * @return Http response to get call
     * @throws IOException An exception if a report is not found
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getAllZapReports(@ModelAttribute(IS_AUTHENTICATED_STRING) boolean isAuthenticated) throws IOException {
        if (!isAuthenticated) {
            return new ResponseEntity(new ErrorMsg(AUTH_NOT_CORRECT_STRING), HttpStatus.BAD_REQUEST);
        }

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
    ResponseEntity<?> getReportById(@ModelAttribute(IS_AUTHENTICATED_STRING) boolean isAuthenticated, @PathVariable("reportId") long reportId) {
        if (!isAuthenticated) {
            return new ResponseEntity(new ErrorMsg(AUTH_NOT_CORRECT_STRING), HttpStatus.BAD_REQUEST);
        }

        ClairReport report = clairService.getReportById(reportId);
        if (report != null) {
            return new ResponseEntity<>(report, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMsg("report not found"), HttpStatus.NOT_FOUND);
        }
    }
}
