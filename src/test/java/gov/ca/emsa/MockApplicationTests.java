package gov.ca.emsa;

import gov.ca.emsa.controller.MockController;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MockTestConfig.class)
@WebAppConfiguration
public class MockApplicationTests {

	@InjectMocks MockController dc;

	private MockMvc mvc;

	public void setup() {
		 MockitoAnnotations.initMocks(this);
		 mvc = MockMvcBuilders.standaloneSetup(dc).build();
	}
	
	@Test
	public void MockTest() throws Exception{
		setup();
		mvc.perform(get("/mock"))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}

}
