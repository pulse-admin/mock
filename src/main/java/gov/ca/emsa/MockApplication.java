package gov.ca.emsa;

import gov.ca.emsa.service.HIEPatientSearchService;

import java.util.Random;
import java.util.Timer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@SpringBootApplication
public class MockApplication {
	
	@Value("${minimumResponseSeconds}")
	private String minimumResponseSeconds;
	
	@Value("${maximumResponseSeconds}")
	private String maximumResponseSeconds;
	
	@Value("${serviceUrl}")
	private String serviceUrl;
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(MockApplication.class, args);
	}
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
	    return new PropertySourcesPlaceholderConfigurer();
	}
	
	@Bean
	public HIEPatientSearchService patientSearchManager() {
		int minimumSeconds = new Integer(minimumResponseSeconds.trim());
		int maximumSeconds = new Integer(maximumResponseSeconds.trim());
		
		Random random = new Random();
		Timer timer = new Timer();
		
		long patientSearchIntervalMillis = random.nextInt(maximumSeconds - minimumSeconds + 1) + minimumSeconds * 1000;
		
		HIEPatientSearchService psTask = null;

		if(minimumSeconds > 0 && maximumSeconds > 0) {
			psTask = new HIEPatientSearchService();
			psTask.setExpirationMillis(patientSearchIntervalMillis);
			psTask.setServiceUrl(serviceUrl);
			
			timer.schedule(psTask, patientSearchIntervalMillis);

		}
		
		return psTask;
	}
}
