package com.xebia.vulnmanager.controller;

import com.xebia.vulnmanager.auth.AuthenticationChecker;
import com.xebia.vulnmanager.models.net.ErrorMsg;
import com.xebia.vulnmanager.models.openvas.objects.OpenvasReport;
import com.xebia.vulnmanager.models.openvas.objects.OvResult;
import com.xebia.vulnmanager.repositories.OpenvasRepository;
import com.xebia.vulnmanager.repositories.OpenvasResultRepository;
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

    @Autowired
    private OpenvasRepository openvasRepository;

    @Autowired
    private OpenvasResultRepository openvasResultRepository;

    // This function is called before other functions, so if for example getReport is called it first runs the init function
    @ModelAttribute("isAuthenticated")
    boolean setAuthenticateBoolean(@RequestHeader(value = "auth", defaultValue = "nope") String authKey,
                                   @PathVariable("company") String companyName,
                                   @PathVariable("team") String teamName) {
        AuthenticationChecker authenticationChecker = new AuthenticationChecker();
        return authenticationChecker.checkTeamAndCompany(companyName, authKey, teamName);
    }

    private OpenvasReport getOpenvasReportFromObject(Object parsedDocument) throws ClassCastException {
        try {
            if (!(parsedDocument instanceof OpenvasReport)) {
                throw new ClassCastException("Object was not of type OpenvasReport");
            }
        } catch (ClassCastException exception) {
            logger.error(exception.getMessage());
            return null;
        }

        return (OpenvasReport) parsedDocument;
    }

    /**
     * Get all the added reports
     *
     * @return A response with correct http header
     * @throws IOException An exception if the example log isn't found
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getReport(@ModelAttribute("isAuthenticated") boolean isAuthenticated) throws IOException {
        if (!isAuthenticated) {
            return new ResponseEntity(new ErrorMsg("Auth not correct!"), HttpStatus.BAD_REQUEST);
        }

        List<OpenvasReport> reportList = openvasRepository.findAll();

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
    ResponseEntity<?> getReport(@ModelAttribute("isAuthenticated") boolean isAuthenticated, @PathVariable("reportId") long reportId) throws IOException {
        if (!isAuthenticated) {
            return new ResponseEntity(new ErrorMsg("Auth not correct!"), HttpStatus.BAD_REQUEST);
        }

        if (openvasRepository.findById(reportId).isPresent()) {
            OpenvasReport retReport = openvasRepository.findById(reportId).get();
            return new ResponseEntity<>(retReport, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMsg("report not found"), HttpStatus.OK);
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
    ResponseEntity<?> getAllResult(@PathVariable("reportid") long reportId,
                                @ModelAttribute("isAuthenticated") boolean isAuthenticated) throws IOException {
        if (!isAuthenticated) {
            return new ResponseEntity(new ErrorMsg("Auth not correct!"), HttpStatus.BAD_REQUEST);
        }

        if (openvasRepository.findById(reportId).isPresent()) {
            OpenvasReport retReport = openvasRepository.findById(reportId).get();
                return new ResponseEntity<>(retReport.getResults(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMsg("report not found"), HttpStatus.OK);
        }
    }

    /**
     * Get a certain result from a specific report
     * @param id The index id in the OpenvasReport of the result.
     * @param reportId The id of the report
     * @param isAuthenticated If the user is authenticated
     * @return Return a result of a report
     * @throws IOException
     */
    @RequestMapping(value = "{reportid}/result/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getResult(@PathVariable("id") long id,
                                @PathVariable("reportid") long reportId,
                                @ModelAttribute("isAuthenticated") boolean isAuthenticated) throws IOException {
        if (!isAuthenticated) {
            return new ResponseEntity(new ErrorMsg("Auth not correct!"), HttpStatus.BAD_REQUEST);
        }

        if (openvasRepository.findById(reportId).isPresent()) {
            OpenvasReport retReport = openvasRepository.findById(reportId).get();
            if (retReport.getResults().stream()
                    .noneMatch((e) -> e.getId().equals(id))) {
                return new ResponseEntity<>(new ErrorMsg("Result not found"), HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(retReport.getResults().stream()
                        .filter((e) -> e.getId().equals(id))
                        .findFirst()
                        .get(),
                        HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(new ErrorMsg("report not found"), HttpStatus.OK);
        }
    }

    /**
     *
     * @param id The index id in the OpenvasReport of the result.
     * @param reportId The id of the report
     * @param isAuthenticated If the user is authenticated
     * @return Return a result of a report
     * @throws IOException
     */
    @RequestMapping(value = "{reportid}/result/{id}/toggle", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> updateResult(@PathVariable("id") long id,
                                @PathVariable("reportid") long reportId,
                                @ModelAttribute("isAuthenticated") boolean isAuthenticated) throws IOException {
        if (!isAuthenticated) {
            return new ResponseEntity(new ErrorMsg("Auth not correct!"), HttpStatus.BAD_REQUEST);
        }

        if (openvasResultRepository.findById(id).isPresent()) {
            OvResult result = openvasResultRepository.findById(id).get();
            result.getNvt().setFalsePositive(!result.getNvt().isFalsePositive());
            result = openvasResultRepository.save(result);

            return new ResponseEntity<>(result, HttpStatus.OK);

        } else {
            return new ResponseEntity<>(new ErrorMsg("result not found"), HttpStatus.OK);
        }
    }
}
