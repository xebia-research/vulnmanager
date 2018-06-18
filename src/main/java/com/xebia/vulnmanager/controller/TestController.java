package com.xebia.vulnmanager.controller;

import com.xebia.vulnmanager.models.clair.objects.ClairReport;
import com.xebia.vulnmanager.models.company.Company;
import com.xebia.vulnmanager.models.company.Person;
import com.xebia.vulnmanager.models.company.Team;
import com.xebia.vulnmanager.models.generic.GenericMultiReport;
import com.xebia.vulnmanager.models.generic.GenericReport;
import com.xebia.vulnmanager.models.net.ErrorMsg;
import com.xebia.vulnmanager.models.net.TestInfoResponse;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping(value = "/addtest")
public class TestController {
    private final Logger logger = LoggerFactory.getLogger("TestController");

    @Autowired
    private OpenvasRepository openvasRepository;
    @Autowired
    private NMapRepository nMapRepository;
    @Autowired
    private OwaspZapRepository zapRepository;
    @Autowired
    private GenericRepository genericRepository;
    @Autowired
    private ClairRepository clairRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private PersonRepository personRepository;

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> deleteEveryThing() {
        TestInfoResponse response = new TestInfoResponse();

        companyRepository.deleteAll();
        genericRepository.deleteAll();
        openvasRepository.deleteAll();
        nMapRepository.deleteAll();
        zapRepository.deleteAll();
        clairRepository.deleteAll();
        companyRepository.deleteAll();
        personRepository.deleteAll();

        response.setCompany(companyRepository.findAll().size() > 0);
        response.setReports(genericRepository.findAll().size() > 0);
        response.setAccounts(personRepository.findAll().size() > 0);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/done", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getIsCompanyAdded() {
        TestInfoResponse response = new TestInfoResponse();

        response.setCompany(companyRepository.findAll().size() > 0);
        response.setReports(genericRepository.findAll().size() > 0);
        response.setAccounts(personRepository.findAll().size() > 0);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/company", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> addCompany() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Person p = personRepository.findByUsername(currentPrincipalName);

        if (p != null) {
            Company company = new Company("xebia");
            company.addEmployee(p);
            p.setCompany(company);

            Team newTeam = new Team("vulnmanager");
            newTeam.addTeamMember(p);
            newTeam.setCompany(company);
            company.addTeam(newTeam);

            return new ResponseEntity<>(companyRepository.save(company), HttpStatus.OK);
        }

        return new ResponseEntity<>(new ErrorMsg("User not found"), HttpStatus.NOT_FOUND);
    }

    /**
     * @return A list of teams within the team
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> addTest() throws ParserConfigurationException, SAXException, IOException {
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

        Company xebiaComp = companyRepository.findByName("xebia").get(0);

        report.setTeam(xebiaComp.findTeamByName("vulnmanager"));
        OpenvasReport retReport = openvasRepository.save(report);

        GenericMultiReport multiReport = retReport.getGenericMultiReport();

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
