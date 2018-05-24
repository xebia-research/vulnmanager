package com.xebia.vulnmanager.test;

import com.xebia.vulnmanager.auth.AuthenticationChecker;
import com.xebia.vulnmanager.controller.NMapController;
import com.xebia.vulnmanager.controller.UploadFileController;
import com.xebia.vulnmanager.data.MockCompanyFactory;
import com.xebia.vulnmanager.services.CompanyService;
import com.xebia.vulnmanager.services.NmapService;
import com.xebia.vulnmanager.util.ReportUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class UploadControllerTest {
    private static final String BASE_URL = "http://localhost:8080";

    private MockMvc mvc;

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private UploadFileController uploadFileController;

    @Mock
    private AuthenticationChecker authenticationChecker;

    @Before
    public void setup() throws Exception {
        // Mock the authentication check to always return true
        Mockito.when(authenticationChecker.checkCompanyAuthKey(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        Mockito.when(authenticationChecker.checkTeamAndCompany(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        Mockito.when(authenticationChecker.checkIfTeamExists(Mockito.any(), Mockito.anyString())).thenReturn(true);

        // Mock the database service to return a list of valid objects
        MockCompanyFactory companyFactory = new MockCompanyFactory();
        Mockito.when(companyService.getCompanyByName("xebia")).thenReturn(companyFactory.getMockCompanies().get(0));
        Mockito.when(companyService.getTeamOfCompany("xebia", "vulnmanager"))
                .thenReturn(companyFactory.getMockCompanies().get(0).getTeams().get(0));

        mvc = MockMvcBuilders.standaloneSetup(uploadFileController)
                .build();
    }

    @Test
    public void unknownReportXmlFile() throws Exception {
        final InputStream inputStream = new FileInputStream(new File("example_logs/lesswrong.xml"));
        final MockMultipartFile nmapReport = new MockMultipartFile("file", "nmap.xml", "text/xml", inputStream);

        final MvcResult result = mvc.perform(fileUpload(BASE_URL + "/xebia/vulnmanager/upload.json")
                .file(nmapReport))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.msg", is("Unknown report!")))
                .andReturn();
    }
}
