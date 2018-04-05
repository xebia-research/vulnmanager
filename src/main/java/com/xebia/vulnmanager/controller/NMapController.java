package com.xebia.vulnmanager.controller;

import com.xebia.vulnmanager.auth.AuthenticationChecker;
import com.xebia.vulnmanager.models.net.ErrorMsg;
import com.xebia.vulnmanager.models.nmap.objects.Host;
import com.xebia.vulnmanager.models.nmap.objects.NMapGeneralInformation;
import com.xebia.vulnmanager.models.nmap.objects.NMapReport;
import com.xebia.vulnmanager.repositories.NMapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static java.lang.Math.toIntExact;

@RestController
@RequestMapping(value = "/{company}/{team}/nmap")
public class NMapController {
    private static final String IS_AUTHENTICATED_STRING = "isAuthenticated";
    private static final String AUTH_NOT_CORRECT_STRING = "Auth not correct!";

    private final Logger logger = LoggerFactory.getLogger("NMapController");

    @Autowired
    private NMapRepository nMapRepository;

    @ModelAttribute(IS_AUTHENTICATED_STRING)
    boolean setAuthenticateBoolean(@RequestHeader(value = "auth", defaultValue = "nope") String authKey,
                                   @PathVariable("company") String companyName,
                                   @PathVariable("team") String teamName) {
        AuthenticationChecker authenticationChecker = new AuthenticationChecker();
        return authenticationChecker.checkTeamAndCompany(companyName, authKey, teamName);
    }

    /**
     * Get all the added reports
     *
     * @return A response with correct http header
     * @throws IOException An exception if the example log isn't found
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getAllNMapReports(@ModelAttribute(IS_AUTHENTICATED_STRING) boolean isAuthenticated) throws IOException {
        if (!isAuthenticated) {
            return new ResponseEntity(new ErrorMsg(AUTH_NOT_CORRECT_STRING), HttpStatus.BAD_REQUEST);
        }

        List<NMapReport> reportList = nMapRepository.findAll();
        if (reportList.isEmpty()) {
            return new ResponseEntity(new ErrorMsg("There are no reports for nMap right now. Upload a nMap xml file first."), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(reportList, HttpStatus.OK);
    }

    /**
     * Get a parsed report of nmap
     *
     * @return A response with correct http header
     * @throws IOException An exception if the example log isn't found
     */
    @RequestMapping(value = "/{reportId}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getNMapReport(@ModelAttribute(IS_AUTHENTICATED_STRING) boolean isAuthenticated, @PathVariable("reportId") long reportId) throws IOException {
        if (!isAuthenticated) {
            return new ResponseEntity<>(new ErrorMsg(AUTH_NOT_CORRECT_STRING), HttpStatus.BAD_REQUEST);
        }

        if (nMapRepository.findById(reportId).isPresent()) {
            NMapReport nMapReport = nMapRepository.findById(reportId).get();
            return new ResponseEntity<>(nMapReport, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMsg("Nmap report not found"), HttpStatus.OK);
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
    ResponseEntity<?> getAllHostsOfReport(@ModelAttribute(IS_AUTHENTICATED_STRING) boolean isAuthenticated, @PathVariable("reportId") long reportId) throws IOException {
        if (!isAuthenticated) {
            return new ResponseEntity<>(new ErrorMsg(AUTH_NOT_CORRECT_STRING), HttpStatus.BAD_REQUEST);
        }

        if (nMapRepository.findById(reportId).isPresent()) {
            NMapReport nMapReport = nMapRepository.findById(reportId).get();
            List<Host> nMapHosts = nMapReport.getHosts();
            return new ResponseEntity<>(nMapHosts, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMsg("Nmap report not found"), HttpStatus.OK);
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
    ResponseEntity<?> getHostOfReport(@ModelAttribute(IS_AUTHENTICATED_STRING) boolean isAuthenticated, @PathVariable("reportId") long reportId,
                                      @PathVariable("hostId") long hostId) throws IOException {
        if (!isAuthenticated) {
            return new ResponseEntity<>(new ErrorMsg(AUTH_NOT_CORRECT_STRING), HttpStatus.BAD_REQUEST);
        }

        int hostIdInt;

        try {
            hostIdInt = toIntExact(hostId);
            hostIdInt = hostIdInt - 1;
        } catch (ArithmeticException arithmeticException) {
            logger.error(arithmeticException.getMessage());
            return new ResponseEntity<>(new ErrorMsg("The id that was filled in is to big."), HttpStatus.OK);
        }

        if (nMapRepository.findById(reportId).isPresent()) {
            NMapReport nMapReport = nMapRepository.findById(reportId).get();
            List<Host> nMapHosts = nMapReport.getHosts();
            Host host;

            try {
                host = nMapHosts.get(hostIdInt);
            } catch (IndexOutOfBoundsException indexOutOfBounds) {
                logger.error(indexOutOfBounds.getMessage());
                return new ResponseEntity<>(new ErrorMsg("The host with this id is not found"), HttpStatus.OK);
            }
            return new ResponseEntity<>(host, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMsg("Nmap report not found"), HttpStatus.OK);
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
    ResponseEntity<?> getScanData(@ModelAttribute(IS_AUTHENTICATED_STRING) boolean isAuthenticated, @PathVariable("reportId") long reportId) throws IOException {
        if (!isAuthenticated) {
            return new ResponseEntity<>(new ErrorMsg(AUTH_NOT_CORRECT_STRING), HttpStatus.BAD_REQUEST);
        }

        if (nMapRepository.findById(reportId).isPresent()) {
            NMapReport nMapReport = nMapRepository.findById(reportId).get();
            NMapGeneralInformation nMapScanData = nMapReport.getScanData();
            return new ResponseEntity<>(nMapScanData, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMsg("Nmap report not found"), HttpStatus.OK);
        }
    }
}
