package com.xebia.vulnmanager.test;

import com.xebia.vulnmanager.Main;
import com.xebia.vulnmanager.auth.AuthenticationChecker;
import com.xebia.vulnmanager.controller.OpenvasController;
import com.xebia.vulnmanager.controller.TestController;
import com.xebia.vulnmanager.repositories.OpenvasRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Uses main.class because it has the startup of the application
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class OpenvasControllerTest {

    @Autowired
    private MockMvc mvc;

    @Mock
    private OpenvasRepository openvasRepo;

    @InjectMocks
    private OpenvasController openvasController;

    private final String BASE_URL = "http://localhost:8080";

    @Mock
    private TestController testController;

    @Mock
    private AuthenticationChecker authenticationChecker;

    @Before
    public void setup() throws Exception {
        testController.addTest();
        // MockMvc standalone approach
        mvc = MockMvcBuilders.standaloneSetup(openvasController, authenticationChecker)
                .build();
    }

    @Test
    public void openvasWithAuthOk() throws Exception {
        /*
        mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/xebia/vulnmanager/openvas.json")
                .header("auth", "testauth"))
                .andDo(print())
                .andExpect(status().isOk());
        */
    }

    @Test
    public void openvasWithNoAuth() throws Exception {
        /*
        mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/xebia/vulnmanager/openvas.json"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"msg\":\"Auth not correct!\"}"));
        */
    }


}