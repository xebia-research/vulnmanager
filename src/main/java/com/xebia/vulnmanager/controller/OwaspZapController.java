package com.xebia.vulnmanager.controller;

import com.xebia.vulnmanager.auth.AuthenticationChecker;
import com.xebia.vulnmanager.models.net.ErrorMsg;
import com.xebia.vulnmanager.models.zap.objects.ScannedSiteInformation;
import com.xebia.vulnmanager.models.zap.objects.ZapReport;
import com.xebia.vulnmanager.services.OwaspZapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/{company}/{team}/zap")
public class OwaspZapController {
    private static final String IS_AUTHENTICATED_STRING = "isAuthenticated";
    private static final String AUTH_NOT_CORRECT_STRING = "Auth not correct!";

    private final Logger logger = LoggerFactory.getLogger("OwaspZapController");

    private AuthenticationChecker authenticationChecker;

    private OwaspZapService owaspZapService;

    @Autowired
    public OwaspZapController(final OwaspZapService owaspZapService,
                              final AuthenticationChecker authenticationChecker) {
        this.owaspZapService = owaspZapService;
        this.authenticationChecker = authenticationChecker;
    }

    /**
     * This function is called before other functions, so if for example getReport is called it first runs the init function
     *
     * @param authKey Key of the user
     * @param companyName Name of the company of the user
     * @param teamName Name of the team of the user
     * @return returns boolean, true if the company, team and authentication are correct
     */
    @ModelAttribute(IS_AUTHENTICATED_STRING)
    boolean setAuthenticateBoolean(@RequestHeader(value = "auth", defaultValue = "nope") String authKey,
                                   @PathVariable("company") String companyName, @PathVariable("team") String teamName) {
        return authenticationChecker.checkTeamAndCompany(companyName, authKey, teamName);
    }

    /**
     * Get all Owasp Zap reports
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

        List<ZapReport> reportList = owaspZapService.getAllReports();
        return new ResponseEntity<>(reportList, HttpStatus.OK);
    }

    /**
     * Get a parsed test report of Owasp Zap
     *
     * @return A response with correct http header
     * @throws IOException An exception if the example log isn't found
     */
    @RequestMapping(value = "{reportId}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getReportById(@ModelAttribute(IS_AUTHENTICATED_STRING) boolean isAuthenticated, @PathVariable("reportId") long reportId) {
        if (!isAuthenticated) {
            return new ResponseEntity(new ErrorMsg(AUTH_NOT_CORRECT_STRING), HttpStatus.BAD_REQUEST);
        }

        ZapReport report = owaspZapService.getReportById(reportId);
        if (report != null) {
            return new ResponseEntity<>(report, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMsg("report not found"), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get a all the sties from a report.
     *
     * @param reportId The id of the OwaspZapReport.
     * @return A response with correct http header
     * @throws IOException Exception when example log isn't found or couldn't be opened
     */
    @RequestMapping(value = "{reportid}/sites", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getAllSitesFromReport(@ModelAttribute(IS_AUTHENTICATED_STRING) boolean isAuthenticated, @PathVariable("reportid") long reportId) throws IOException {
        if (!isAuthenticated) {
            return new ResponseEntity(new ErrorMsg(AUTH_NOT_CORRECT_STRING), HttpStatus.BAD_REQUEST);
        }

        ZapReport report = owaspZapService.getReportById(reportId);
        if (report != null) {
            return new ResponseEntity<>(report.getScannedSitesInformation(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMsg("report not found"), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get a certain site from a specific report
     *
     * @param siteId          The index of the site.
     * @param reportId        The id of the report
     * @param isAuthenticated If the user is authenticated
     * @return Return a result of a report
     * @throws IOException
     */
    @RequestMapping(value = "{reportid}/sites/{siteid}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getResult(@ModelAttribute(IS_AUTHENTICATED_STRING) boolean isAuthenticated,
                                @PathVariable("siteid") long siteId,
                                @PathVariable("reportid") long reportId) throws IOException {
        if (!isAuthenticated) {
            return new ResponseEntity(new ErrorMsg(AUTH_NOT_CORRECT_STRING), HttpStatus.BAD_REQUEST);
        }

        ScannedSiteInformation siteInformation = owaspZapService.getSiteInformationFromReportById(reportId, siteId);

        if (siteInformation == null) {
            return new ResponseEntity<>(new ErrorMsg("Report or site with this id not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(siteInformation, HttpStatus.OK);
    }
}
