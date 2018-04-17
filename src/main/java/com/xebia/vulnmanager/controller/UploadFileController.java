package com.xebia.vulnmanager.controller;

import com.xebia.vulnmanager.auth.AuthenticationChecker;
import com.xebia.vulnmanager.models.company.Company;
import com.xebia.vulnmanager.models.company.Team;
import com.xebia.vulnmanager.models.net.ErrorMsg;
import com.xebia.vulnmanager.models.nmap.objects.NMapReport;
import com.xebia.vulnmanager.models.openvas.objects.OpenvasReport;
import com.xebia.vulnmanager.repositories.NMapRepository;
import com.xebia.vulnmanager.repositories.OpenvasRepository;
import com.xebia.vulnmanager.services.CompanyService;
import com.xebia.vulnmanager.util.IOUtil;
import com.xebia.vulnmanager.util.ReportType;
import com.xebia.vulnmanager.util.ReportUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping(value = "/{company}/{team}/{scannerType}/upload")
public class UploadFileController {
    private final Logger logger = LoggerFactory.getLogger("UploadFileController");

    @Autowired
    private OpenvasRepository openvasRepository;

    @Autowired
    private NMapRepository nMapRepository;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private AuthenticationChecker authenticationChecker;

    /**
     * Upload a report to the server.
     *
     * @param uploadFile The file that will be uploaded
     * @return A response with correct http header
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile uploadFile,
                                 @RequestHeader(value = "auth", defaultValue = "nope") String authKey,
                                 @PathVariable("company") String companyName,
                                 @PathVariable("team") String teamName,
                                 @PathVariable("scannerType") String scannerType) {

        if (!authenticationChecker.checkTeamAndCompany(companyName, authKey, teamName)) {
            return new ResponseEntity(new ErrorMsg("Auth not correct!"), HttpStatus.BAD_REQUEST);
        }

        // Check if the parser type endpoint exists.
        if (!isValidScannerType(scannerType)) {
            return new ResponseEntity(new ErrorMsg("Unknown parser type"), HttpStatus.BAD_REQUEST);
        }

        // Shouldn't return null because the authenticationChecker als checks for null.
        Company comp = companyService.getCompanyByName(companyName);
        Team team = companyService.getTeamOfCompany(companyName, teamName);

        if (team == null || comp == null) {
            return new ResponseEntity(new ErrorMsg("Auth not correct!"), HttpStatus.BAD_REQUEST);
        }

        logger.info("Single file upload started!");
        String newFileName = "";
        if (uploadFile.isEmpty()) {
            return new ResponseEntity(new ErrorMsg("Uploaded file should't be empty"), HttpStatus.BAD_REQUEST);
        }

        try {
            // IOUtil will try to save the file. Returns true on succes
            String filePath = IOUtil.saveUploadedFiles(uploadFile);
            File tempFile = new File(filePath);

            // Success with upload. Check file to see of it is a {scannerType} document
            logger.info("File succesfully uploaded");
            boolean wrongEndpoint = false;

            // Success check uploaded file
            ReportType reportType = ReportUtil.checkDocumentType(ReportUtil.getDocumentFromFile(tempFile));
            if (reportType != ReportType.UNKNOWN) {
                if (reportType.toString().equalsIgnoreCase(scannerType)) {
                    newFileName = IOUtil.moveFileToFolder(tempFile, comp, team, reportType);

                    // Get company and team;
                    uploadFileToDB(tempFile, reportType, team);


                } else {
                    // File is known but wrong endpoint
                    wrongEndpoint = true;
                }
            }

            if (!tempFile.delete()) {
                logger.error("Temp file couldn't be deleted");
            }

            // Separate if to delete the tmp file
            if (reportType == ReportType.UNKNOWN) {
                // Type unknown send bad request.
                return new ResponseEntity(new ErrorMsg("Unknown report!"), HttpStatus.BAD_REQUEST);
            } else if (wrongEndpoint) {
                return new ResponseEntity(new ErrorMsg("This is a " + reportType.name() + " report but this endpoint expects a " + scannerType), HttpStatus.BAD_REQUEST);
            }

        } catch (IOException ex) {
            return new ResponseEntity(new ErrorMsg("IOException with msg: " + ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(new ErrorMsg("Successfully uploaded - " + newFileName), HttpStatus.OK);
    }

    /**
     * Check if the specified input string is a valid type.
     *
     * @param input Input from the url param with the name of the parser
     * @return Will return true if the parser name is known in the ReportType enum
     */
    private boolean isValidScannerType(String input) {
        for (ReportType type : ReportType.values()) {
            if (type.name().equalsIgnoreCase(input)) {
                return true;
            }
        }
        return false;
    }

    private void uploadFileToDB(File uploadFile, ReportType reportType, Team team) {
        Document document = ReportUtil.getDocumentFromFile(uploadFile);
        Object parsedDocument = ReportUtil.parseDocument(document);
        if (reportType == ReportType.OPENVAS) {
            OpenvasReport openvasReport = ReportUtil.getOpenvasReportFromObject(parsedDocument);
            if (openvasReport == null) {
                return;
            }

            // Save the report
            openvasReport.setTeam(team);
            openvasRepository.save(openvasReport);
            openvasRepository.flush();
        } else if (reportType == ReportType.NMAP) {
            NMapReport nMapReport = ReportUtil.getNMapReportFromObject(parsedDocument);
            if (nMapReport == null) {
                return;
            }

            // Save the report
            nMapRepository.save(nMapReport);
            nMapRepository.flush();
        }
    }
}
