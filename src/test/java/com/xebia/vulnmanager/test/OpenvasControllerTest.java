package com.xebia.vulnmanager.test;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.xebia.vulnmanager.auth.AuthenticationChecker;
import com.xebia.vulnmanager.controller.OpenvasController;
import com.xebia.vulnmanager.models.openvas.objects.OpenvasReport;
import com.xebia.vulnmanager.models.openvas.objects.OvResult;
import com.xebia.vulnmanager.services.OpenvasService;
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

/**
 * Uses main.class because it has the startup of the application
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class OpenvasControllerTest {

    private static final String BASE_URL = "http://localhost:8080";

    private MockMvc mvc;

    @Mock
    private OpenvasService openvasService;

    @InjectMocks
    private OpenvasController openvasController;

    @Mock
    private AuthenticationChecker authenticationChecker;

    @Before
    public void setup() throws Exception {
        // Mock the authentication check to always return true
        Mockito.when(authenticationChecker.checkCompanyAuthKey(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        Mockito.when(authenticationChecker.checkTeamAndCompany(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        Mockito.when(authenticationChecker.checkIfTeamExists(Mockito.any(), Mockito.anyString())).thenReturn(true);

        // Load test report
        Object parsedDocument = ReportUtil.parseDocument(ReportUtil.getDocumentFromFile(new File("example_logs/openvas/openvas.xml")));
        Object parsedDocument2 = ReportUtil.parseDocument(ReportUtil.getDocumentFromFile(new File("example_logs/openvas/openvas.xml")));
        // Create the reports
        List<OpenvasReport> reports = new ArrayList<OpenvasReport>();
        reports.add(ReportUtil.getOpenvasReportFromObject(parsedDocument));
        reports.add(ReportUtil.getOpenvasReportFromObject(parsedDocument2));

        // Set id to reports en results
        long id = 1L;
        for (int i = 0; i < reports.size(); i++) {
            OpenvasReport report = reports.get(i);
            // set the id of the report and increment for the next one
            report.setId(id++);
            for (OvResult result : report.getResults()) {
                // set the id of the result
                result.setId(id++);
            }
        }

        // Mock the database service to return a list of valid objects
        Mockito.when(openvasService.getAllReports()).thenReturn(reports);

        // For any long return null except for other rules
        // Order of the rules matter
        Mockito.when(openvasService.getFromRaportByIdResultById(Mockito.anyLong(), Mockito.anyLong())).thenReturn(null);
        Mockito.when(openvasService.getReportById(Mockito.anyLong())).thenReturn(null);
        for (int i = 0; i < reports.size(); i++) {
            OpenvasReport report = reports.get(i);
            // The other rules
            Mockito.when(openvasService.getReportById(report.getId())).thenReturn(report);
            for (OvResult result : report.getResults()) {
                System.out.println(report.getId() + " -- " + result.getId());
                Mockito.when(openvasService.getFromRaportByIdResultById(report.getId(), result.getId())).thenReturn(result);
            }
        }


        // MockMvc standalone approach
        mvc = MockMvcBuilders.standaloneSetup(openvasController)
                .build();
    }

    @Test
    public void getAllReports() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/xebia/vulnmanager/openvas.json")
                .header("auth", "testauth"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void getReportFromId() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/xebia/vulnmanager/openvas/1.json")
                .header("auth", "testauth"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getUnknownReportById() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/xebia/vulnmanager/openvas/3.json")
                .header("auth", "testauth"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAllResultsFromReport() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/xebia/vulnmanager/openvas/1/result.json")
                .header("auth", "testauth"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getResultByIdFromReport() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/xebia/vulnmanager/openvas/1/result/3.json")
                .header("auth", "testauth"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getUnknownResultByIdFromReport() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/xebia/vulnmanager/openvas/1/result/1.json")
                .header("auth", "testauth"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void getUnknownResultByIdFromUnknownReport() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/xebia/vulnmanager/openvas/2/result/1.json")
                .header("auth", "testauth"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


}