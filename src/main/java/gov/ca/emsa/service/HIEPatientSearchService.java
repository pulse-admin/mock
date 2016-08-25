package gov.ca.emsa.service;

import gov.ca.emsa.xcpd.XcpdUtils;
import gov.ca.emsa.xcpd.soap.DiscoveryRequestSoapEnvelope;
import gov.ca.emsa.xcpd.soap.DiscoveryResponseSoapEnvelope;
import gov.ca.emsa.xcpd.soap.QueryRequestSoapEnvelope;
import gov.ca.emsa.xcpd.soap.QueryResponseSoapEnvelope;
import gov.ca.emsa.xcpd.soap.RetrieveDocumentSetRequestSoapEnvelope;
import gov.ca.emsa.xcpd.soap.RetrieveDocumentSetResponseSoapEnvelope;

import java.util.TimerTask;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.client.RestTemplate;

public class HIEPatientSearchService extends TimerTask {
	private static final Logger logger = LogManager.getLogger(HIEPatientSearchService.class);

	private String serviceUrl;
	private long intervalMillis;

	public void sendPatientSearchRequest() throws Exception{
		RestTemplate restTemplate = new RestTemplate();
		DiscoveryRequestSoapEnvelope request = XcpdUtils.generateDiscoveryRequest("Brian", "Lindsey");
		DiscoveryResponseSoapEnvelope patientDiscoveryResponse = restTemplate.postForObject(serviceUrl + "/patientDiscovery", request, DiscoveryResponseSoapEnvelope.class);
		logger.info(patientDiscoveryResponse);
		logger.info("HIE sending patient discovery search query with SOAP...");
	}
	
	public void sendQueryRequest() throws Exception{
		RestTemplate restTemplate = new RestTemplate();
		QueryRequestSoapEnvelope request = XcpdUtils.generateQueryRequest();
		QueryResponseSoapEnvelope queryResponse = restTemplate.postForObject(serviceUrl + "/queryRequest", request, QueryResponseSoapEnvelope.class);
		logger.info(queryResponse);
		logger.info("HIE sending query patient search with SOAP...");
	}
	
	public void sendDocumentSetRequest() throws Exception{
		RestTemplate restTemplate = new RestTemplate();
		RetrieveDocumentSetRequestSoapEnvelope request = XcpdUtils.generateDocumentRequest();
		RetrieveDocumentSetResponseSoapEnvelope queryResponse = restTemplate.postForObject(serviceUrl + "/documentRequest", request, RetrieveDocumentSetResponseSoapEnvelope.class);
		logger.info(queryResponse);
		logger.info("HIE sending document set search query with SOAP...");
	}

	@Override
	public void run() {
		try {
			sendPatientSearchRequest();
			sendQueryRequest();
			sendDocumentSetRequest();
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
