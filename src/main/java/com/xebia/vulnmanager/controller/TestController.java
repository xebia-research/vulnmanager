package com.xebia.vulnmanager.controller;

import com.xebia.vulnmanager.data.MockCompanyFactory;
import com.xebia.vulnmanager.models.comments.Comment;
import com.xebia.vulnmanager.models.company.Company;
import com.xebia.vulnmanager.models.generic.GenericMultiReport;
import com.xebia.vulnmanager.models.generic.GenericReport;
import com.xebia.vulnmanager.models.clair.objects.ClairReport;
import com.xebia.vulnmanager.models.net.ErrorMsg;
import com.xebia.vulnmanager.models.nmap.objects.NMapReport;
import com.xebia.vulnmanager.models.openvas.objects.OpenvasReport;
import com.xebia.vulnmanager.models.zap.objects.ZapReport;
import com.xebia.vulnmanager.repositories.*;
import com.xebia.vulnmanager.util.ReportUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/addtest")
public class TestController {
    private final Logger logger = LoggerFactory.getLogger("TestController");

    @Autowired
    private OpenvasRepository openvasRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private NMapRepository nMapRepository;
    @Autowired
    private OwaspZapRepository zapRepository;
    @Autowired
    private GenericRepository genericRepository;
    @Autowired
    private ClairRepository clairRepository;

    /**
     * @return A list of teams within the team
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> addTest() {
        Object parsedDocument = ReportUtil.parseDocument(ReportUtil.getDocumentFromFile(new File("example_logs/openvas/openvas.xml")));
        OpenvasReport report = ReportUtil.getOpenvasReportFromObject(parsedDocument);

        parsedDocument = ReportUtil.parseDocument(ReportUtil.getDocumentFromFile(new File("example_logs/nmap/nmap.xml")));
        NMapReport nMapReport = ReportUtil.getNMapReportFromObject(parsedDocument);

        parsedDocument = ReportUtil.parseDocument(ReportUtil.getDocumentFromFile(new File("example_logs/owasp_zap/Kopano_web_app.xml")));
        ZapReport zapReport = ReportUtil.getZapReportFromObject(parsedDocument);

        parsedDocument = ReportUtil.parseDocument(ReportUtil.getDocumentFromFile(new File("example_logs/clair/clair_scan_radarr.json")));
        ClairReport clairReport = ReportUtil.getClairReportFromObject(parsedDocument);

        nMapRepository.save(nMapReport);
        zapRepository.save(zapReport);
        clairRepository.save(clairReport);

        if (report == null) {
            return new ResponseEntity(new ErrorMsg("The file requested is not of the right type"), HttpStatus.BAD_REQUEST);
        }

        Company xebiaComp = companyRepository.save(new MockCompanyFactory().getMockCompanies().get(0));
        report.setTeam(companyRepository.findAll().get(1).findTeamByName("vulnmanager"));
        OpenvasReport retReport = openvasRepository.save(report);

        List<Comment> testComments = new ArrayList<>();
        Comment test = new Comment();
        test.setContent("Hello world!");
        test.setUser(xebiaComp.getTeams().get(0).getTeamMembers().get(0));
        testComments.add(test);

        GenericMultiReport multiReport = retReport.getGenericMultiReport();
        multiReport.getReports().get(0).getGenericResults().get(0).setComments(testComments);

        GenericMultiReport nmap = nMapReport.getMultiReport();
        for (GenericReport genRep : nmap.getReports()) {
            multiReport.addReports(genRep);
        }

        GenericMultiReport zap = zapReport.getGenericReport();
        for (GenericReport genRep : zap.getReports()) {
            multiReport.addReports(genRep);
        }

        GenericMultiReport clair = clairReport.getGenericReport();
        for (GenericReport genRep : clair.getReports()) {
            multiReport.addReports(genRep);
        }

        // Save the generic reports
        for (GenericReport genRep : multiReport.getReports()) {
            genericRepository.save(genRep);
        }

        multiReport = new GenericMultiReport(genericRepository.findAll());

        return new ResponseEntity<>(multiReport.getReports(), HttpStatus.OK);
    }
}
