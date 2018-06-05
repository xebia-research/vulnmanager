package com.xebia.vulnmanager.controller;

import com.xebia.vulnmanager.models.generic.GenericMultiReport;
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

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(value = "/{company}/{team}/nmap")
public class NMapController {
    private static final String REPORT_NOT_FOUND_LITERAL = "Nmap report not found";
    private static final String REPORT_ID_LITERAL = "reportId";

    private final Logger logger = LoggerFactory.getLogger("NMapController");

    private NmapService nmapService;

    @Autowired
    public NMapController(final NmapService nmapService) {
        this.nmapService = nmapService;
    }
    /**
     * Get all the added reports
     *
     * @return A response with correct http header
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getAllNMapReports() {

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
     */
    @RequestMapping(value = "/{reportId}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getNMapReport(@PathVariable(REPORT_ID_LITERAL) long reportId) {

        Optional<NMapReport> report = nmapService.getReportById(reportId);
        if (report.isPresent()) {
            NMapReport nMapReport = report.get();
            return new ResponseEntity<>(nMapReport, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMsg(REPORT_NOT_FOUND_LITERAL), HttpStatus.OK);
        }
    }

    /**
     * Get all hosts of parsed test report
     *
     * @return A response with correct http header
     */
    @RequestMapping(value = "/{reportId}/hosts", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getAllHostsOfReport(@PathVariable(REPORT_ID_LITERAL) long reportId) {

        Optional<NMapReport> report = nmapService.getReportById(reportId);
        if (report.isPresent()) {
            NMapReport nMapReport = report.get();
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
     */
    @RequestMapping(value = "/{reportId}/hosts/{hostId}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getHostOfReport(@PathVariable(REPORT_ID_LITERAL) long reportId,
                                      @PathVariable("hostId") long hostId) {

        Optional<NMapReport> report = nmapService.getReportById(reportId);
        if (report.isPresent()) {
            NMapReport nMapReport = report.get();
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
     */
    @RequestMapping(value = "/{reportId}/scanData", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getScanData(@PathVariable(REPORT_ID_LITERAL) long reportId) {

        Optional<NMapReport> report = nmapService.getReportById(reportId);

        if (report.isPresent()) {
            NMapReport nMapReport = report.get();
            NMapGeneralInformation nMapScanData = nMapReport.getScanData();
            return new ResponseEntity<>(nMapScanData, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMsg(REPORT_NOT_FOUND_LITERAL), HttpStatus.OK);
        }
    }

    /**
     * Get all the added reports
     *
     * @return A response with correct http header
     * @throws IOException An exception if the example log isn't found
     */
    @RequestMapping(value = "generic", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getGenericReports() throws IOException {

        GenericMultiReport reportList = nmapService.getAllReportsAsGeneric();

        return new ResponseEntity<>(reportList, HttpStatus.OK);
    }
}
