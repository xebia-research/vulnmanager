package com.xebia.vulnmanager.controller;

import com.xebia.vulnmanager.models.clair.objects.ClairReport;
import com.xebia.vulnmanager.models.company.Company;
import com.xebia.vulnmanager.models.company.Team;
import com.xebia.vulnmanager.models.generic.GenericReport;
import com.xebia.vulnmanager.models.net.ErrorMsg;
import com.xebia.vulnmanager.models.nmap.objects.NMapReport;
import com.xebia.vulnmanager.models.openvas.objects.OpenvasReport;
import com.xebia.vulnmanager.models.zap.objects.ZapReport;
import com.xebia.vulnmanager.repositories.*;
import com.xebia.vulnmanager.services.CompanyService;
import com.xebia.vulnmanager.util.IOUtil;
import com.xebia.vulnmanager.util.ReportType;
import com.xebia.vulnmanager.util.ReportUtil;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping(value = "/{company}/{team}/upload")
public class UploadFileController {
    private final Logger logger = LoggerFactory.getLogger("UploadFileController");

    @Autowired
    private OpenvasRepository openvasRepository;

    @Autowired
    private NMapRepository nMapRepository;

    @Autowired
    private OwaspZapRepository zapRepository;

    @Autowired
    private ClairRepository clairRepository;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private GenericRepository genericRepository;


    /**
     * Upload a report to the server.
     *
     * @param uploadFiles The file that will be uploaded
     * @return A response with correct http header
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile[] uploadFiles,
                                 @PathVariable("company") String companyName,
                                 @PathVariable("team") String teamName) {

        // Shouldn't return null because the authenticationChecker als checks for null.
        Company comp = companyService.getCompanyByName(companyName);
        Team team = companyService.getTeamOfCompany(companyName, teamName);

        if (team == null || comp == null) {
            return new ResponseEntity(new ErrorMsg("Auth not correct!"), HttpStatus.BAD_REQUEST);
        }

        for (MultipartFile uploadFile : uploadFiles) {

            logger.info("Single file upload started!");
            String newFileName;
            if (uploadFile.isEmpty()) {
                return new ResponseEntity(new ErrorMsg("Uploaded file should't be empty"), HttpStatus.BAD_REQUEST);
            }

            try {
                // IOUtil will try to save the file. Returns true on succes
                String filePath = IOUtil.saveUploadedFiles(uploadFile);
                File tempFile = new File(filePath);

                // Success with upload. Check file to see of it is a {scannerType} document
                logger.info("File succesfully uploaded");

                // Success check uploaded file
                Document reportDocument = ReportUtil.getDocumentFromFile(tempFile);
                ReportType reportType = ReportUtil.checkDocumentType(reportDocument);
                if (reportType == ReportType.UNKNOWN) {
                    return new ResponseEntity(new ErrorMsg("Unknown report!"), HttpStatus.BAD_REQUEST);
                }
                newFileName = IOUtil.moveFileToFolder(tempFile, comp, team, reportType);

                // Get company and team;
                uploadFileToDB(reportDocument, reportType, team);

                if (!tempFile.delete()) {
                    logger.error("Temp file couldn't be deleted");
                }
            } catch (IOException ex) {
                logger.error(ex.getMessage());
                return new ResponseEntity(new ErrorMsg("IOException with msg: " + ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (ParserConfigurationException e) {
                logger.error(e.getMessage());
                return new ResponseEntity(new ErrorMsg("Parser configuration exception wit the message: " + e.getMessage()), HttpStatus.BAD_REQUEST);
            } catch (SAXException e) {
                logger.error(e.getMessage());
                return new ResponseEntity(new ErrorMsg("SAX basic exception with the message: " + e.getMessage()), HttpStatus.BAD_REQUEST);
            } catch (NullPointerException e) {
                logger.error("Null pointer exception was thrown");
                return new ResponseEntity(new ErrorMsg("An null pointer exception was thrown"), HttpStatus.BAD_REQUEST);
            } catch (JSONException e) {
                logger.error(e.getMessage());
                return new ResponseEntity(new ErrorMsg("A JSON exception was thrown with message:" + e.getMessage()), HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity(new ErrorMsg("Successfully uploaded "), HttpStatus.OK);
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

    private void uploadFileToDB(Document document, ReportType reportType, Team team) {
        if (document == null) {
            return;
        }
        Object parsedDocument = ReportUtil.parseDocument(document);
        if (reportType == ReportType.OPENVAS) {
            OpenvasReport openvasReport = ReportUtil.getOpenvasReportFromObject(parsedDocument);
            if (openvasReport == null) {
                return;
            }

            // Save the report
            openvasReport.setTeam(team);
            openvasReport = openvasRepository.save(openvasReport);
            openvasRepository.flush();
            for (GenericReport report : openvasReport.getGenericMultiReport().getReports()) {
                genericRepository.save(report);
            }
            genericRepository.flush();

        } else if (reportType == ReportType.NMAP) {
            NMapReport nMapReport = ReportUtil.getNMapReportFromObject(parsedDocument);
            if (nMapReport == null) {
                return;
            }

            // Save the report
            nMapReport = nMapRepository.save(nMapReport);
            nMapRepository.flush();
            for (GenericReport report : nMapReport.getMultiReport().getReports()) {
                genericRepository.save(report);
            }
            genericRepository.flush();
        } else if (reportType == ReportType.ZAP) {
            ZapReport zapReport = ReportUtil.getZapReportFromObject(parsedDocument);
            if (zapReport == null) {
                return;
            }

            // Save the report
            zapReport = zapRepository.save(zapReport);
            zapRepository.flush();
            for (GenericReport report : zapReport.getGenericReport().getReports()) {
                genericRepository.save(report);
            }
            genericRepository.flush();
        } else if (reportType == ReportType.CLAIR) {
            ClairReport clairReport = ReportUtil.getClairReportFromObject(parsedDocument);
            if (clairReport == null) {
                return;
            }

            // Save the report
            clairReport = clairRepository.save(clairReport);
            clairRepository.flush();
            for (GenericReport report : clairReport.getGenericReport().getReports()) {
                genericRepository.save(report);
            }
            genericRepository.flush();
        }
    }
}
