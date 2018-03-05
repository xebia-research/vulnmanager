import com.xebia.vulnmanager.Main;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
/**
 * Uses main.class because it has the startup of the application
 */
@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
public class OpenvasControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void getReport() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/openvas/"))
                .andExpect(status().isOk());
    }

}