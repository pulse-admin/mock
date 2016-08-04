package gov.ca.emsa.service;

import gov.ca.emsa.SOAPGenerator;
import gov.ca.emsa.pulse.common.domain.Organization;
import gov.ca.emsa.pulse.common.domain.Patient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TimerTask;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.client.RestTemplate;

public class HIEPatientSearchService extends TimerTask {
	private static final Logger logger = LogManager.getLogger(HIEPatientSearchService.class);

	private String serviceUrl;
	private long intervalMillis;

	public void sendSearchQuery() throws Exception{
		RestTemplate restTemplate = new RestTemplate();
		SOAPGenerator sg = new SOAPGenerator();
		sg.addHeader();
		sg.addQueryForPatient("Brian","Lindsey");
		//Patient[] patients = restTemplate.postForObject(serviceUrl, sg, Patient[].class);
		//logger.info(patients.toString());
		System.out.println("HIE sending search query with SOAP...");
		sg.getMessage().writeTo(System.out);
	}
	
	public void sendQueryOfPatientList() throws Exception{
		RestTemplate restTemplate = new RestTemplate();
		SOAPGenerator sg = new SOAPGenerator();
		sg.addHeader();
		sg.addQueryOfPatientList(new ArrayList<String>());
		//Patient[] patients = restTemplate.postForObject(serviceUrl, sg, Patient[].class);
		//logger.info(patients.toString());
		System.out.println("HIE sending search query with SOAP...");
		sg.getMessage().writeTo(System.out);
	}
	
	public void sendQueryOfDocumentList() throws Exception{
		RestTemplate restTemplate = new RestTemplate();
		SOAPGenerator sg = new SOAPGenerator();
		sg.addHeader();
		sg.addQueryOfDocumentList(new ArrayList<String>());
		//Patient[] patients = restTemplate.postForObject(serviceUrl, sg, Patient[].class);
		//logger.info(patients.toString());
		System.out.println("HIE sending search query with SOAP...");
		sg.getMessage().writeTo(System.out);
	}

	@Override
	public void run() {
		try {
			sendSearchQuery();
			sendQueryOfPatientList();
			sendQueryOfDocumentList();
		} catch(Exception ex) {
			logger.error("Error sending SOAP search query", ex);
		}
	}

	public void setExpirationMillis(long hieSendSearchQueryExpirationMillis) {
		this.intervalMillis = hieSendSearchQueryExpirationMillis;
	}

	public long getExpirationMillis() {
		return intervalMillis;
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}
}
