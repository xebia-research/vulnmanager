package com.xebia.vulnmanager.controller;

import com.xebia.vulnmanager.models.generic.GenericMultiReport;
import com.xebia.vulnmanager.models.net.ErrorMsg;
import com.xebia.vulnmanager.models.openvas.objects.OpenvasReport;
import com.xebia.vulnmanager.models.openvas.objects.OvResult;
import com.xebia.vulnmanager.repositories.OpenvasResultRepository;
import com.xebia.vulnmanager.services.OpenvasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping(value = "/{company}/{team}/openvas")
public class OpenvasController {
    private final Logger logger = LoggerFactory.getLogger("OpenvasController");

    private OpenvasResultRepository openvasResultRepository;

    private OpenvasService openvasService;

    @Autowired
    public OpenvasController(final OpenvasService openvasService,
                             final OpenvasResultRepository openvasResultRepository) {
        this.openvasService = openvasService;
        this.openvasResultRepository = openvasResultRepository;
    }

    /**
     * Get all the added reports
     *
     * @return A response with correct http header
     * @throws IOException An exception if the example log isn't found
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getReports() throws IOException {

        List<OpenvasReport> reportList = openvasService.getAllReports();

        return new ResponseEntity<>(reportList, HttpStatus.OK);
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
        GenericMultiReport reportList = openvasService.getAllReportsAsGeneric();

        return new ResponseEntity<>(reportList, HttpStatus.OK);
    }

    /**
     * Get a parsed test report of openvas
     *
     * @return A response with correct http header
     * @throws IOException An exception if the example log isn't found
     */
    @RequestMapping(value = "{reportId}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getReportById(@PathVariable("reportId") long reportId) {
        OpenvasReport report = openvasService.getReportById(reportId);
        if (report != null) {
            return new ResponseEntity<>(report, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMsg("report not found"), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get a all the results from a report.
     *
     * @param reportId The  id of the OpenvasReport.
     * @return A response with correct http header
     * @throws IOException Exception when example log isn't found or couldn't be opened
     */
    @RequestMapping(value = "{reportid}/result", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getAllResultFromReport(@PathVariable("reportid") long reportId) throws IOException {
        OpenvasReport report = openvasService.getReportById(reportId);
        if (report != null) {
            return new ResponseEntity<>(report.getResults(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMsg("report not found"), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get a certain result from a specific report
     *
     * @param id              The index id in the OpenvasReport of the result.
     * @param reportId        The id of the report
     * @param isAuthenticated If the user is authenticated
     * @return Return a result of a report
     * @throws IOException
     */
    @RequestMapping(value = "{reportid}/result/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getResult(@PathVariable("id") long id,
                                @PathVariable("reportid") long reportId) throws IOException {

        OvResult result = openvasService.getFromRaportByIdResultById(reportId, id);

        if (result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMsg("Report or Result not found"), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * @param id              The index id in the OpenvasReport of the result.
     * @param reportId        The id of the report
     * @param isAuthenticated If the user is authenticated
     * @return Return a result of a report
     * @throws IOException
     */
    @RequestMapping(value = "{reportid}/result/{id}/toggle", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> updateResult(@PathVariable("id") long id,
                                   @PathVariable("reportid") long reportId) throws IOException {

        if (openvasResultRepository.findById(id).isPresent()) {
            OvResult result = openvasResultRepository.findById(id).get();
            result.getNvt().setFalsePositive(!result.getNvt().isFalsePositive());
            result = openvasResultRepository.save(result);

            return new ResponseEntity<>(result, HttpStatus.OK);

        } else {
            return new ResponseEntity<>(new ErrorMsg("result not found"), HttpStatus.NOT_FOUND);
        }
    }
}
