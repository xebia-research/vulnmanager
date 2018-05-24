package com.xebia.vulnmanager.test;

import com.xebia.vulnmanager.auth.AuthenticationChecker;
import com.xebia.vulnmanager.controller.CompanyController;
import com.xebia.vulnmanager.controller.NMapController;
import com.xebia.vulnmanager.data.MockCompanyFactory;
import com.xebia.vulnmanager.models.company.Company;
import com.xebia.vulnmanager.models.nmap.objects.NMapReport;
import com.xebia.vulnmanager.services.CompanyService;
import com.xebia.vulnmanager.services.NmapService;
import com.xebia.vulnmanager.util.ReportUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class CompanyControllerTest {
    private static final String BASE_URL = "http://localhost:8080";

    private MockMvc mvc;

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private CompanyController companyController;

    @Before
    public void setup() throws Exception {
        // Load test report
        Object parsedDocument = ReportUtil.parseDocument(ReportUtil.getDocumentFromFile(new File("example_logs/nmap/nmap.xml")));
        // Create the reports

        MockCompanyFactory companyFactory = new MockCompanyFactory();

        // Mock the database service to return a list of valid objects
        Mockito.when(companyService.getCompanyByName("xebia")).thenReturn(companyFactory.getMockCompanies().get(0));
        Mockito.when(companyService.getTeamOfCompany("xebia", "vulnmanager"))
                .thenReturn(companyFactory.getMockCompanies().get(0).getTeams().get(0));

        // MockMvc standalone approach
        mvc = MockMvcBuilders.standaloneSetup(companyController)
                .build();
    }

    @Test
    public void getCompanyByName() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/xebia.json")
                .header("auth", "testauth"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getCompanyWrongAuth() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/xebia.json")
                .header("auth", "wrongauthkey"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getCompanyWrongByName() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/thisunknowncompany.json")
                .header("auth", "testauth"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void getTeam() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/xebia/vulnmanager.json")
                .header("auth", "testauth"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getUnknownTeam() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/xebia/unknownteam.json")
                .header("auth", "testauth"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
