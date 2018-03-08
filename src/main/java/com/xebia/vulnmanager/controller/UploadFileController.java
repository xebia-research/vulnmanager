package com.xebia.vulnmanager.controller;

import com.xebia.vulnmanager.auth.AuthenticationChecker;
import com.xebia.vulnmanager.data.MockCompanyFactory;
import com.xebia.vulnmanager.models.company.Company;
import com.xebia.vulnmanager.models.company.Team;
import com.xebia.vulnmanager.models.net.ErrorMsg;
import com.xebia.vulnmanager.util.IOUtil;
import com.xebia.vulnmanager.util.ReportType;
import com.xebia.vulnmanager.util.ReportUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Controller
public class UploadFileController {
    private final Logger logger = LoggerFactory.getLogger("UploadFileController");

    /**
     * Upload a report to the server.
     *
     * @param uploadFile The file that will be uploaded
     * @return A response with correct http header
     */
    @RequestMapping(value = "/{company}/{team}/{scannerType}/upload", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile uploadFile,
                                 @RequestHeader(value = "auth", defaultValue = "nope") String authKey,
                                 @PathVariable("company") String companyName,
                                 @PathVariable("team") String teamName,
                                 @PathVariable("scannerType") String scannerType) {

        AuthenticationChecker authenticationChecker = new AuthenticationChecker();
        if (!authenticationChecker.checkTeamAndCompany(companyName, authKey, teamName)) {
            return new ResponseEntity(new ErrorMsg("Auth not correct!"), HttpStatus.BAD_REQUEST);
        }

        // Shouldn't return null because the authenticationChecker als checks for null.
        MockCompanyFactory factory = new MockCompanyFactory();
        Company comp = factory.findCompanyByName(companyName);
        Team team = factory.findTeamByName(teamName, comp);

        logger.info("Single file upload started!");
        String newFileName = "";
        if (uploadFile.isEmpty()) {
            return new ResponseEntity(new ErrorMsg("Uploaded file should't be empty"), HttpStatus.BAD_REQUEST);
        }

        try {
            // IOUtil will try to save the file. Returns true on succes
            String filePath = IOUtil.saveUploadedFiles(uploadFile);

            // Success with upload. Check file to see of it is a {scannerType} document
            logger.info("File succesfully uploaded");

            // Success check uploaded file
            ReportType reportType = ReportUtil.checkDocumentType(ReportUtil.getDocumentFromFile(new File(filePath)));
            if (reportType != ReportType.UNKNOWN && Objects.equals(reportType.toString().toLowerCase(), scannerType.toLowerCase())) {
                newFileName = IOUtil.moveFileToFolder(new File(filePath), comp, team, reportType);
            }

            File fileToRemove = new File(filePath);
            if (!fileToRemove.delete()) {
                logger.error("Temp file couldn't be deleted but it shoudl have been");
            }

            // Separate if to delete the tmp file
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
