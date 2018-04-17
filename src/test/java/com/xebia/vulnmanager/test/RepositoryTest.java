package com.xebia.vulnmanager.test;

import com.xebia.vulnmanager.data.MockCompanyFactory;
import com.xebia.vulnmanager.models.company.Company;
import com.xebia.vulnmanager.models.company.Team;
import com.xebia.vulnmanager.models.nmap.objects.NMapReport;
import com.xebia.vulnmanager.models.openvas.objects.OpenvasReport;
import com.xebia.vulnmanager.repositories.CompanyRepository;
import com.xebia.vulnmanager.repositories.NMapRepository;
import com.xebia.vulnmanager.repositories.OpenvasRepository;
import com.xebia.vulnmanager.util.ReportUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Uses main.class because it has the startup of the application
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class RepositoryTest {

    @Autowired
    private OpenvasRepository openvasRepository;

    @Autowired
    private NMapRepository nMapRepository;

    @Autowired
    private CompanyRepository companyRepository;


    @Before
    public void setup() throws Exception {
        Object parsedDocument = ReportUtil.parseDocument(ReportUtil.getDocumentFromFile(new File("example_logs/openvas.xml")));
        OpenvasReport report = ReportUtil.getOpenvasReportFromObject(parsedDocument);

        parsedDocument = ReportUtil.parseDocument(ReportUtil.getDocumentFromFile(new File("example_logs/nmap.xml")));
        NMapReport nMapReport = ReportUtil.getNMapReportFromObject(parsedDocument);
        nMapRepository.save(nMapReport);

        companyRepository.save(new MockCompanyFactory().getMockCompanies().get(0));
        report.setTeam(companyRepository.findAll().get(0).findTeamByName("vulnmanager"));
        OpenvasReport retReport = openvasRepository.save(report);
        openvasRepository.flush();
        nMapRepository.flush();
        companyRepository.flush();
    }

    @Test
    public void companyGetByName() throws Exception {
        List<Company> found = companyRepository.findByName("xebia");

        assertThat(found.size() > 0);
        assertThat(found.get(0).getName().equalsIgnoreCase("xebia"));
    }


    @Test
    public void teamGetByName() throws Exception {
        List<Company> foundList = companyRepository.findByName("xebia");
        Company found = foundList.get(0);
        Team team = found.findTeamByName("vulnmanager");
        assertThat(team.getName().equalsIgnoreCase("vulnmanager"));
    }

    @Test
    public void getOpenvasReport() throws Exception {
        List<Company> foundList = companyRepository.findByName("xebia");
        Company found = foundList.get(0);
        Team team = found.findTeamByName("vulnmanager");
        List<OpenvasReport> reports = openvasRepository.findAll();
        assertThat(reports.size() > 0);
        assertThat(reports.get(0).getTeam().getCompany().getName().equalsIgnoreCase(team.getCompany().getName()));
    }

}