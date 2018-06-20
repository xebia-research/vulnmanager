package com.xebia.vulnmanager.test;

import com.xebia.vulnmanager.controller.NMapController;
import com.xebia.vulnmanager.models.nmap.objects.NMapReport;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
public class NmapControllerTest {

    private static final String BASE_URL = "http://localhost:8080";

    private MockMvc mvc;

    @Mock
    private NmapService nmapService;

    @InjectMocks
    private NMapController nMapController;

    @Before
    public void setup() throws Exception {
        // Load test report
        Object parsedDocument = ReportUtil.parseDocument(ReportUtil.getDocumentFromFile(new File("example_logs/nmap/nmap.xml")));
        // Create the reports
        List<NMapReport> reports = new ArrayList<>();
        reports.add(ReportUtil.getNMapReportFromObject(parsedDocument));
        reports.get(0).setId(1L);


        // Mock the database service to return a list of valid objects
        Mockito.when(nmapService.getAllReports()).thenReturn(reports);

        NMapReport report = reports.get(0);

        Mockito.when(nmapService.getReportById(1L)).thenReturn(report);

        // MockMvc standalone approach
        mvc = MockMvcBuilders.standaloneSetup(nMapController)
                .build();
    }

    @Test
    public void getAllReports() throws Exception {
        /*
        mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/xebia/vulnmanager/nmap/1.json")
                .header("auth", "testauth"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
                */
    }
}
