package com.xebia.vulnmanager.controller;

import com.xebia.vulnmanager.auth.AuthenticationChecker;
import com.xebia.vulnmanager.data.MockCompanyFactory;
import com.xebia.vulnmanager.models.company.Company;
import com.xebia.vulnmanager.models.company.Team;
import com.xebia.vulnmanager.models.net.ErrorMsg;
import com.xebia.vulnmanager.models.openvas.objects.OpenvasReport;
import com.xebia.vulnmanager.models.openvas.objects.OvResult;
import com.xebia.vulnmanager.util.IOUtil;
import com.xebia.vulnmanager.util.ReportType;
import com.xebia.vulnmanager.util.ReportUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping(value = "/{company}/{team}/openvas")
public class OpenvasController {

    private final Logger logger = Logger.getLogger("OpenvasController");

    /**
     * Get a parsed test report of openvas
     * @return A response with correct http header
     * @throws IOException An exception if the example log isn't found
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getReport(@RequestHeader(value = "auth", defaultValue = "nope") String authKey,
                                @PathVariable("company") String companyName,
                                @PathVariable("team") String teamName) throws IOException {
        AuthenticationChecker authenticationChecker = new AuthenticationChecker();
        if (!authenticationChecker.checkTeamAndCompany(companyName, authKey, teamName)) {
            return new ResponseEntity(new ErrorMsg("Auth not correct!"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<OpenvasReport>(ReportUtil.parseDocument(ReportUtil.getDocumentFromFile(new File("example_logs/openvas.xml"))), HttpStatus.OK);
    }

    /**
     * Get a certain result from a report.
     * @param id The index id in the OpenvasReport.
     * @return A response with correct http header
     * @throws IOException Exception when example log isn't found or couldn't be opened
     */
    @RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getResult(@PathVariable("id") int id,
                                @RequestHeader(value = "auth", defaultValue = "nope") String authKey,
                                @PathVariable("company") String companyName,
                                @PathVariable("team") String teamName) throws IOException {
        AuthenticationChecker authenticationChecker = new AuthenticationChecker();
        if (!authenticationChecker.checkTeamAndCompany(companyName, authKey, teamName)) {
            return new ResponseEntity(new ErrorMsg("Auth not correct!"), HttpStatus.BAD_REQUEST);
        }

        OpenvasReport report = ReportUtil.parseDocument(ReportUtil.getDocumentFromFile(new File("example_logs/openvas.xml")));

        if (report != null && id >= report.getResults().size() || id < 0) {
            return new ResponseEntity<ErrorMsg>(new ErrorMsg("Result not found"), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<OvResult>(report.getResults().get(id), HttpStatus.OK);
        }
    }

    /**
     * Upload a report to the server.
     * @param uploadfile The file that will be uploaded
     * @return A response with correct http header
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile uploadfile,
                                 @RequestHeader(value = "auth", defaultValue = "nope") String authKey,
                                 @PathVariable("company") String companyName,
                                 @PathVariable("team") String teamName) {

        AuthenticationChecker authenticationChecker = new AuthenticationChecker();
        if (!authenticationChecker.checkTeamAndCompany(companyName, authKey, teamName)) {
            return new ResponseEntity(new ErrorMsg("Auth not correct!"), HttpStatus.BAD_REQUEST);
        }

        // Shouldn't return null because the authenticationChecker als checks for null.
        MockCompanyFactory factory = new MockCompanyFactory();
        Company comp = factory.findCompanyByName(companyName);
        Team team = factory.findTeamByName(teamName, comp);

        logger.log(Level.FINE, "Single file upload started!");
        String newFileName = "";
        if (uploadfile.isEmpty()) {
            return new ResponseEntity(new ErrorMsg("Uploaded file should't be empty"), HttpStatus.BAD_REQUEST);
        }

        try {
            // IOUttil will try to save the file. Returns true on succes
            String filePath = IOUtil.saveUploadedFiles(uploadfile);

            //Succes with upload. Check file to see of it is a openvas document
            logger.log(Level.FINE, "File succesfully uploaded");

            // succes check uploaded file
            ReportType reportType = ReportUtil.checkDocumentType(ReportUtil.getDocumentFromFile(new File(filePath)));
            if (reportType != ReportType.UNKNOWN) {
                newFileName = IOUtil.moveFileToFolder(new File(filePath), comp, team, reportType);
            }

            File fileToRemove = new File(filePath);
            if (!fileToRemove.delete()) {
                logger.log(Level.FINE, "Temp file couldn't be deleted but it shoudl have been");
            }

            // Seperate if to delete the tmp file
            if (reportType == ReportType.UNKNOWN) {
                // Type unknown send bad request.
                return new ResponseEntity(new ErrorMsg("Unknown report!"), HttpStatus.BAD_REQUEST);
            }

        } catch (IOException ex) {
            return new ResponseEntity(new ErrorMsg("IOException with msg: " + ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(new ErrorMsg("Successfully uploaded - " + newFileName), HttpStatus.OK);
    }
}
