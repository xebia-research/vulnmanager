package com.xebia.vulnmanager.controller;

import com.xebia.vulnmanager.data.MockCompanyFactory;
import com.xebia.vulnmanager.models.net.ErrorMsg;
import com.xebia.vulnmanager.models.openvas.objects.OpenvasReport;
import com.xebia.vulnmanager.repositories.CompanyRepository;
import com.xebia.vulnmanager.repositories.OpenvasRepository;
import com.xebia.vulnmanager.util.ReportUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@Controller
@RequestMapping(value = "/addtest")
public class TestController {
    private final Logger logger = LoggerFactory.getLogger("TestController");

    @Autowired
    private OpenvasRepository openvasRepository;
    @Autowired
    private CompanyRepository companyRepository;

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
     * @return A list of teams within the team
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> addTest()  {
        Object parsedDocument = ReportUtil.parseDocument(ReportUtil.getDocumentFromFile(new File("example_logs/openvas.xml")));
        OpenvasReport report = getOpenvasReportFromObject(parsedDocument);

        if (report == null) {
            return new ResponseEntity(new ErrorMsg("The file requested is not of the right type"), HttpStatus.BAD_REQUEST);
        }

        companyRepository.save(new MockCompanyFactory().getMockCompanies().get(0));
        report.setTeam(companyRepository.findAll().get(0).findTeamByName("vulnmanager"));
        OpenvasReport retReport = openvasRepository.save(report);

        return new ResponseEntity<>(retReport, HttpStatus.OK);
    }
}
