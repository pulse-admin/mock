package gov.ca.emsa;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@PropertySource("classpath:/application-test.properties")
@SpringBootApplication(scanBasePackages= {"gov.ca.emsa.controller.**", "gov.ca.emsa.domain.**",
		"gov.ca.emsa.service.**", "gov.ca.emsa.pulse.common.**"})
@Configuration
@EnableWebMvc
public class MockTestConfig {}
