import com.xebia.vulnmanager.Main;
import org.junit.Test;
import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import static org.hamcrest.Matchers.equalTo;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Uses main.class because it has the startup of the application
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
public class OpenvasControllerTest {

//    @Autowired
//    private MockMvc mvc;

    @Test
    public void testMainEndpoint() throws Exception {
        /*
        mvc.perform(MockMvcRequestBuilders.get("xebia/vulnmanager/openvas/")
                .header("auth", "testauth"))
                .andExpect(status().isOk());
        */
    }

    @Test
    public void testResultIndex() throws Exception {
        /*
        // Succes
        mvc.perform(MockMvcRequestBuilders.get("xebia/vulnmanager/openvas/result/1")
                .header("auth", "testauth"))
                .andExpect(status().isOk());

        // To low
        mvc.perform(MockMvcRequestBuilders.get("xebia/vulnmanager/openvas/result/-1")
                .header("auth", "testauth"))
                .andExpect(status().isNotFound());

        // To high
        mvc.perform(MockMvcRequestBuilders.get("xebia/vulnmanager/openvas/result/99999")
                .header("auth", "testauth"))
                .andExpect(status().isNotFound());
        */
    }
}