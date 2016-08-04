package gov.ca.emsa;

import gov.ca.emsa.service.HIEPatientSearchService;

import java.util.Timer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MockApplication extends SpringBootServletInitializer {
	
	@Value("${patientSearchInterval}")
	private String patientSearchInterval;
	@Value("${serviceUrl}")
	private String serviceUrl;
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(MockApplication.class, args);
	}
	
	@Bean
	public HIEPatientSearchService patientSearchManager() {
		int searchInterval = new Integer(patientSearchInterval.trim());
		long patientSearchIntervalMillis = searchInterval * 1000;
		
		HIEPatientSearchService psTask = null;

		if(searchInterval > 0) {
			psTask = new HIEPatientSearchService();
			psTask.setExpirationMillis(patientSearchIntervalMillis);
			psTask.setServiceUrl(serviceUrl);
			
			Timer timer = new Timer();
			timer.scheduleAtFixedRate(psTask, 0, patientSearchIntervalMillis);

		}
		
		return psTask;
	}
}
