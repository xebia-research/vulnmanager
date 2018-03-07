package com.xebia.vulnmanager.controller;


import com.xebia.vulnmanager.openvas.objects.OpenvasReport;
import com.xebia.vulnmanager.openvas.objects.OvResult;
import com.xebia.vulnmanager.util.IOUtil;
import com.xebia.vulnmanager.util.ReportType;
import com.xebia.vulnmanager.util.ReportUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping(value = "/openvas")
public class OpenvasController {

    private final Logger logger = Logger.getLogger("OpenvasController");

    /**
     * Get a parsed test report of openvas
     * @return A response with correct http header
     * @throws IOException An exception if the example log isn't found
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getReport() throws IOException {
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
    ResponseEntity<?> getResult(@PathVariable("id") int id) throws IOException {
        OpenvasReport report = ReportUtil.parseDocument(ReportUtil.getDocumentFromFile(new File("example_logs/openvas.xml")));

        if (id >= report.getResults().size() || id < 0) {
            return new ResponseEntity<String>("Error", HttpStatus.NOT_FOUND);
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
    ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile uploadfile) {

        logger.log(Level.FINE, "Single file upload started!");
        String newFileName = "";
        if (uploadfile.isEmpty()) {
            return new ResponseEntity("please select a file!", HttpStatus.BAD_REQUEST);
        }

        try {
            // IOUttil will try to save the file. Returns true on succes
            String filePath = IOUtil.saveUploadedFiles(uploadfile);

            //Succes with upload. Check file to see of it is a openvas document
            logger.log(Level.FINE, "File succesfully uploaded");

            // succes check uploaded file
            ReportType reportType = ReportUtil.checkDocumentType(ReportUtil.getDocumentFromFile(new File(filePath)));
            if (reportType == ReportType.OPENVAS) {
                // Move to openvas dir
                newFileName = IOUtil.moveFileToFolder(new File(filePath), ReportType.OPENVAS);
            }

            File fileToRemove = new File(filePath);
            if (!fileToRemove.delete()) {
                logger.log(Level.FINE, "Temp file couldn't be deleted but it shoudl have been");
            }

            // Seperate if to delete the tmp file
            if (reportType == reportType.UNKNOWN) {
                // Type unknown send bad request.
                return new ResponseEntity("Unknown report!", HttpStatus.BAD_REQUEST);
            }

        } catch (IOException ex) {
            return new ResponseEntity("IOException with msg: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity("Successfully uploaded - " + newFileName, HttpStatus.OK);
    }
}
