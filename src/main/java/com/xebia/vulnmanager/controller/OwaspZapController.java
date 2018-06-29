package com.xebia.vulnmanager.controller;

import com.xebia.vulnmanager.models.generic.GenericMultiReport;
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

    private static final String COMPANY_LITTERAL = "company";
    private static final String TEAM_LITTERAL = "team";

    private final Logger logger = LoggerFactory.getLogger("OwaspZapController");
    private OwaspZapService owaspZapService;

    @Autowired
    public OwaspZapController(final OwaspZapService owaspZapService) {
        this.owaspZapService = owaspZapService;
    }

    /**
     * Get all Owasp Zap reports
     *
     * @return Http response to get call
     * @throws IOException An exception if a report is not found
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getAllZapReports(@PathVariable(COMPANY_LITTERAL) String companyName,
                                       @PathVariable(TEAM_LITTERAL) String teamName) throws IOException {
        List<ZapReport> reportList = owaspZapService.getAllReportsByTeam(companyName, teamName);
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
    ResponseEntity<?> getReportById(@PathVariable("reportId") long reportId) {

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
    ResponseEntity<?> getAllSitesFromReport(@PathVariable("reportid") long reportId) throws IOException {

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
     * @return Return a result of a report
     * @throws IOException
     */
    @RequestMapping(value = "{reportid}/sites/{siteid}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getResult(@PathVariable("siteid") long siteId,
                                @PathVariable("reportid") long reportId) throws IOException {

        ScannedSiteInformation siteInformation = owaspZapService.getSiteInformationFromReportById(reportId, siteId);

        if (siteInformation == null) {
            return new ResponseEntity<>(new ErrorMsg("Report or site with this id not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(siteInformation, HttpStatus.OK);
    }

    /**
     * Get all the added reports
     *
     * @return A response with correct http header
     * @throws IOException An exception if the example log isn't found
     */
    @RequestMapping(value = "generic", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getGenericReports(@PathVariable(COMPANY_LITTERAL) String companyName,
                                        @PathVariable(TEAM_LITTERAL) String teamName) throws IOException {

        GenericMultiReport reportList = owaspZapService.getAllReportsAsGeneric(companyName, teamName);

        return new ResponseEntity<>(reportList, HttpStatus.OK);
    }
}
