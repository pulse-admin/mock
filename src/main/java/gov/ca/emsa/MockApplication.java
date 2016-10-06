package gov.ca.emsa;

import gov.ca.emsa.service.impl.HIEPatientSearchService;

import java.util.Random;
import java.util.Timer;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@SpringBootApplication
public class MockApplication {
	@Value("${patientSearchIntervalSeconds}")
	private String patientSearchIntervalSeconds;
	
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
		int patientSearchIntervalSecondsInt = new Integer(patientSearchIntervalSeconds.trim());
	
		Timer timer = new Timer();
		long patientSearchIntervalMillis = patientSearchIntervalSecondsInt * 1000;
		
		HIEPatientSearchService psTask = null;
		psTask = new HIEPatientSearchService();
		psTask.setServiceUrl(serviceUrl);
		timer.schedule(psTask, patientSearchIntervalMillis, patientSearchIntervalMillis);
		
		return psTask;
	}
}
