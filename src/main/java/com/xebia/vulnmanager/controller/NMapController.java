package com.xebia.vulnmanager.controller;

import com.xebia.vulnmanager.auth.AuthenticationChecker;
import com.xebia.vulnmanager.models.net.ErrorMsg;
import com.xebia.vulnmanager.models.nmap.objects.Host;
import com.xebia.vulnmanager.models.nmap.objects.NMapGeneralInformation;
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
    private static final String IS_AUTHENTICATED_LITERAL = "isAuthenticated";
    private static final String AUTH_NOT_CORRECT_LITERAL = "Auth not correct!";
    private static final String REPORT_NOT_FOUND_LITERAL = "Nmap report not found";
    private static final String REPORT_ID_LITERAL = "reportId";

    private final Logger logger = LoggerFactory.getLogger("NMapController");

    private NmapService nmapService;
    private AuthenticationChecker authenticationChecker;

    @Autowired
    public NMapController(final NmapService nmapService,
                          final AuthenticationChecker authenticationChecker) {
        this.nmapService = nmapService;
        this.authenticationChecker = authenticationChecker;
    }

    @ModelAttribute(IS_AUTHENTICATED_LITERAL)
    boolean setAuthenticateBoolean(@RequestHeader(value = "auth", defaultValue = "nope") String authKey,
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
    ResponseEntity<?> getAllNMapReports(@ModelAttribute(IS_AUTHENTICATED_LITERAL) boolean isAuthenticated) {
        if (!isAuthenticated) {
            return new ResponseEntity(new ErrorMsg(AUTH_NOT_CORRECT_LITERAL), HttpStatus.BAD_REQUEST);
        }

        List<NMapReport> reportList = nmapService.getAllReports();
        if (reportList.isEmpty()) {
            return new ResponseEntity(new ErrorMsg("There are no reports for nMap right now. Upload a nMap xml file first."), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(reportList, HttpStatus.OK);
    }

    /**
     * Get a parsed report of nmap
     *
     * @return A response with correct http header
     * @throws IOException An exception if the example log isn't found
     */
    @RequestMapping(value = "/{reportId}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getNMapReport(@ModelAttribute(IS_AUTHENTICATED_LITERAL) boolean isAuthenticated, @PathVariable(REPORT_ID_LITERAL) long reportId) throws IOException {
        if (!isAuthenticated) {
            return new ResponseEntity<>(new ErrorMsg(AUTH_NOT_CORRECT_LITERAL), HttpStatus.BAD_REQUEST);
        }

        if (nMapRepository.findById(reportId).isPresent()) {
            NMapReport nMapReport = nMapRepository.findById(reportId).get();
            return new ResponseEntity<>(nMapReport, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMsg(REPORT_NOT_FOUND_LITERAL), HttpStatus.OK);
        }
    }

    /**
     * Get all hosts of parsed test report
     *
     * @return A response with correct http header
     * @throws IOException An exception if the example log isn't found
     */
    @RequestMapping(value = "/{reportId}/hosts", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getAllHostsOfReport(@ModelAttribute(IS_AUTHENTICATED_LITERAL) boolean isAuthenticated, @PathVariable(REPORT_ID_LITERAL) long reportId) throws IOException {
        if (!isAuthenticated) {
            return new ResponseEntity<>(new ErrorMsg(AUTH_NOT_CORRECT_LITERAL), HttpStatus.BAD_REQUEST);
        }

        if (nMapRepository.findById(reportId).isPresent()) {
            NMapReport nMapReport = nMapRepository.findById(reportId).get();
            List<Host> nMapHosts = nMapReport.getHosts();
            return new ResponseEntity<>(nMapHosts, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMsg(REPORT_NOT_FOUND_LITERAL), HttpStatus.OK);
        }
    }

    /**
     * Get a specific host of parsed nMap report
     *
     * @return A response with correct http header
     * @throws IOException An exception if the example log isn't found
     */
    @RequestMapping(value = "/{reportId}/hosts/{hostId}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getHostOfReport(@ModelAttribute(IS_AUTHENTICATED_LITERAL) boolean isAuthenticated, @PathVariable(REPORT_ID_LITERAL) long reportId,
                                      @PathVariable("hostId") long hostId) throws IOException {
        if (!isAuthenticated) {
            return new ResponseEntity<>(new ErrorMsg(AUTH_NOT_CORRECT_LITERAL), HttpStatus.BAD_REQUEST);
        }

        if (nMapRepository.findById(reportId).isPresent()) {
            NMapReport nMapReport = nMapRepository.findById(reportId).get();
            List<Host> nMapHosts = nMapReport.getHosts();

            Host chosenHost = null;
            for (Host host : nMapHosts) {
                if (host.getId() == hostId) {
                    chosenHost = host;
                }
            }

            if (chosenHost == null) {
                logger.error("Host with id:" + hostId + " is not found");
                return new ResponseEntity<>(new ErrorMsg("The host with id:" + hostId + " is not found"), HttpStatus.OK);
            }
            return new ResponseEntity<>(chosenHost, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMsg(REPORT_NOT_FOUND_LITERAL), HttpStatus.OK);
        }
    }

    /**
     * Get the general information about a parsed nMap report
     *
     * @return A response with correct http header
     * @throws IOException An exception if the example log isn't found
     */
    @RequestMapping(value = "/{reportId}/scanData", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getScanData(@ModelAttribute(IS_AUTHENTICATED_LITERAL) boolean isAuthenticated, @PathVariable(REPORT_ID_LITERAL) long reportId) throws IOException {
        if (!isAuthenticated) {
            return new ResponseEntity<>(new ErrorMsg(AUTH_NOT_CORRECT_LITERAL), HttpStatus.BAD_REQUEST);
        }

        if (nMapRepository.findById(reportId).isPresent()) {
            NMapReport nMapReport = nMapRepository.findById(reportId).get();
            NMapGeneralInformation nMapScanData = nMapReport.getScanData();
            return new ResponseEntity<>(nMapScanData, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMsg(REPORT_NOT_FOUND_LITERAL), HttpStatus.OK);
        }
    }
}
