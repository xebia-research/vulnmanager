package com.xebia.vulnmanager.controller;

import com.xebia.vulnmanager.auth.AuthenticationChecker;
import com.xebia.vulnmanager.models.comments.Comment;
import com.xebia.vulnmanager.models.comments.CommentRequest;
import com.xebia.vulnmanager.models.company.Person;
import com.xebia.vulnmanager.models.generic.GenericReport;
import com.xebia.vulnmanager.models.generic.GenericResult;
import com.xebia.vulnmanager.models.net.ErrorMsg;
import com.xebia.vulnmanager.services.CommentService;
import com.xebia.vulnmanager.services.GenericReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/{company}/{team}/generic")
public class CommentController {
    private static final String IS_AUTHENTICATED_STRING = "isAuthenticated";
    private static final String AUTH_NOT_CORRECT_STRING = "Auth not correct!";

    private static final String RESULT_NOT_FOUND = "Result not found";
    private static final String RESULT_ID = "resultid";

    private final Logger logger = LoggerFactory.getLogger("CommentController");

    private GenericReportService genericReportService;
    private CommentService commentService;

    private AuthenticationChecker authenticationChecker;


    @Autowired
    public CommentController(final GenericReportService genericReportService,
                             final CommentService commentService,
                             final AuthenticationChecker authenticationChecker) {
        this.genericReportService = genericReportService;
        this.commentService = commentService;
        this.authenticationChecker = authenticationChecker;
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
        List<GenericReport> reportList = genericReportService.getAllReports();

        return new ResponseEntity<>(reportList, HttpStatus.OK);
    }

    /**
     * Get all the added reports
     *
     * @return A response with correct http header
     * @throws IOException An exception if the example log isn't found
     */
    @RequestMapping(value = "report/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getReportById(@PathVariable("id") Long id) throws IOException {

        Optional<GenericReport> reportList = genericReportService.getReportByGenericId(id);
        if (reportList.isPresent()) {
            return new ResponseEntity<>(reportList.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMsg("Report not found"), HttpStatus.OK);
        }
    }

    /**
     * Get all the added reports
     *
     * @return A response with correct http header
     * @throws IOException An exception if the example log isn't found
     */
    @RequestMapping(value = "/report/{id}/result/{resultid}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getResultById(@PathVariable("id") Long id,
                                    @PathVariable(RESULT_ID) Long resultid) throws IOException {

        Optional<GenericResult> reportList = genericReportService.getReportByGenericId(id, resultid);
        if (reportList.isPresent()) {
            return new ResponseEntity<>(reportList.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMsg(RESULT_NOT_FOUND), HttpStatus.OK);
        }
    }

    /**
     * Get all the added reports
     *
     * @return A response with correct http header
     * @throws IOException An exception if the example log isn't found
     */
    @RequestMapping(value = "/report/{id}/result/{resultid}/comment", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getCommentsById(@PathVariable("id") Long id,
                                    @PathVariable(RESULT_ID) Long resultid) throws IOException {

        Optional<GenericResult> reportList = genericReportService.getReportByGenericId(id, resultid);
        if (reportList.isPresent()) {
            return new ResponseEntity<>(reportList.get().getComments(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorMsg(RESULT_NOT_FOUND), HttpStatus.OK);
        }
    }

    /**
     * Get all the added reports
     *
     * @return A response with correct http header
     * @throws IOException An exception if the example log isn't found
     */
    @RequestMapping(value = "/report/{id}/result/{resultid}/comment", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> postComment(@PathVariable("id") Long id,
                                      @PathVariable(RESULT_ID) Long resultid,
                                      @RequestBody CommentRequest comment) throws IOException {
        Optional<GenericResult> reportList = genericReportService.getReportByGenericId(id, resultid);
        if (reportList.isPresent()) {
            Comment created = new Comment();
            created.setParent(reportList.get());
            created.setContent(comment.getContent());
            created.setCreatedAt(LocalDateTime.now());
            Person p = authenticationChecker.checkIfUserExists(comment.getUserName());

            if (p != null) {
                p.setPassword("");
                created.setUser(p);
                reportList.get().addComment(created);
                commentService.saveComments(reportList.get().getReport());
                return new ResponseEntity<>(commentService.saveComments(reportList.get().getReport()), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ErrorMsg("User not found"), HttpStatus.OK);

        } else {
            return new ResponseEntity<>(new ErrorMsg(RESULT_NOT_FOUND), HttpStatus.OK);
        }
    }

    /**
     * Get all the added reports
     *
     * @return A response with correct http header
     * @throws IOException An exception if the example log isn't found
     */
    @RequestMapping(value = "/report/{id}/result/{resultid}/falsePositive", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> postFalsePositive(@PathVariable("id") Long id,
                                  @PathVariable(RESULT_ID) Long resultid) throws IOException {

        Optional<GenericReport> optResult = genericReportService.getReportByGenericId(id);
        if (optResult.isPresent()) {
            GenericReport report = optResult.get();
            List<GenericResult> results = report.getGenericResults();

            GenericResult result = null;

            for (GenericResult res : results) {

                if (res.getId().equals(resultid)) {
                    res.setFalsePositive(!res.isFalsePositive());
                    result = res;
                    break;
                }
            }
            report.setGenericResults(results);
            genericReportService.getGenRepository().save(report);
            return new ResponseEntity<>(result, HttpStatus.OK);

        } else {
            return new ResponseEntity<>(new ErrorMsg("Result not found"), HttpStatus.OK);
        }
    }
}
