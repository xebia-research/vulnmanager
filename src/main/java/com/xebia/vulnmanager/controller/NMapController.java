package com.xebia.vulnmanager.controller;

import com.xebia.vulnmanager.auth.AuthenticationChecker;
import com.xebia.vulnmanager.models.net.ErrorMsg;
import com.xebia.vulnmanager.models.nmap.objects.NMapReport;
import com.xebia.vulnmanager.services.NmapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping(value = "/{company}/{team}/nmap")
public class NMapController {
    private static final String IS_AUTHENTICATED_STRING = "isAuthenticated";
    private static final String AUTH_NOT_CORRECT_STRING = "Auth not correct!";

    private final Logger logger = LoggerFactory.getLogger("NMapController");

    private NmapService nmapService;
    private AuthenticationChecker authenticationChecker;

    @Autowired
    public NMapController(final NmapService nmapService,
                          final AuthenticationChecker authenticationChecker) {
        this.nmapService = nmapService;
        this.authenticationChecker = authenticationChecker;
    }

    @ModelAttribute(IS_AUTHENTICATED_STRING)
    boolean setAuthenticateBoolean(@RequestHeader(value = "auth", defaultValue = "testauth") String authKey,
                                   @PathVariable("company") String companyName,
                                   @PathVariable("team") String teamName) {
        return authenticationChecker.checkTeamAndCompany(companyName, authKey, teamName);
    }

    /**
     * Get all the added reports
     *
     * @return A response with correct http header
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getAllNMapReports(@ModelAttribute(IS_AUTHENTICATED_STRING) boolean isAuthenticated) {
        if (!isAuthenticated) {
            return new ResponseEntity(new ErrorMsg(AUTH_NOT_CORRECT_STRING), HttpStatus.BAD_REQUEST);
        }

        List<NMapReport> reportList = nmapService.getAllReports();
        if (reportList.isEmpty()) {
            return new ResponseEntity(new ErrorMsg("There are no reports for nMap right now. Upload a nMap xml file first."), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(reportList, HttpStatus.OK);
    }
}
