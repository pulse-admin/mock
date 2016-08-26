package gov.ca.emsa.service;

import gov.ca.emsa.saml.SamlGenerator;
import gov.ca.emsa.saml.SAMLInput;
import gov.ca.emsa.xcpd.XcpdUtils;
import gov.ca.emsa.xcpd.soap.DiscoveryRequestSoapEnvelope;
import gov.ca.emsa.xcpd.soap.DiscoveryResponseSoapEnvelope;
import gov.ca.emsa.xcpd.soap.QueryRequestSoapEnvelope;
import gov.ca.emsa.xcpd.soap.QueryResponseSoapEnvelope;
import gov.ca.emsa.xcpd.soap.RetrieveDocumentSetRequestSoapEnvelope;
import gov.ca.emsa.xcpd.soap.RetrieveDocumentSetResponseSoapEnvelope;

import java.util.HashMap;
import java.util.TimerTask;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.opensaml.xml.io.MarshallingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class HIEPatientSearchService extends TimerTask {
	private static final Logger logger = LogManager.getLogger(HIEPatientSearchService.class);

	private String serviceUrl;
	private long intervalMillis;
	@Autowired SamlGenerator samlGenerator;
	
	public String createSamlHeader(){
		SAMLInput input = new SAMLInput();
		input.setAssertionId("12345678-1234-1234-012345678910");
		input.setStrNameID("Brian");
		input.setStrNameQualifier("My Website");
		input.setSessionId("abcdedf1234567");

		HashMap<String, String> customAttributes = new HashMap<String,String>();
		//subject name
		customAttributes.put("urn:oasis:names:tc:xspa:1.0:subject:subject-id", "John");
		customAttributes.put("urn:oasis:names:tc:xspa:1.0:subject:organization", "Medical Clinic One");
		customAttributes.put("urn:oasis:names:tc:xspa:1.0:subject:organization-id", "medicalclinic.org");
		customAttributes.put("urn:nhin:names:saml:homeCommunityId", "urn:oid:2.16.840.1.113883.3.190");
		customAttributes.put("urn:oasis:names:tc:xacml:2.0:subject:role", "<Role xmlns=\"urn:hl7-org:v3\" xsi:type=\"CE\" code=\"46255001\"codeSystem=\"2.16.840.1.113883.6.96\" codeSystemName=\"SNOMED_CT\"displayName=\"Pharmacist\"/>");
		customAttributes.put("urn:oasis:names:tc:xspa:1.0:subject:purposeofuse", "<PurposeOfUse xmlns=\"urn:hl7-org:v3\" xsi:type=\"CE\" code=\"DISASTER\"codeSystem=\"2.16.840.1.113883.3.18.7.1\" codeSystemName=\"nhin-purpose\"displayName=\"Use and disclosures for disaster relief purposes\"/>");
		input.setAttributes(customAttributes);
		
		String samlMessage = null;

		try {
			samlMessage = samlGenerator.createSAML(input);
		} catch (MarshallingException e) {
			e.printStackTrace();
		}
		
		return samlMessage;
	}

	public void sendPatientSearchRequest() throws Exception{
		RestTemplate restTemplate = new RestTemplate();
		DiscoveryRequestSoapEnvelope request = XcpdUtils.generateDiscoveryRequest(createSamlHeader(), "Brian", "Lindsey");
		logger.info(request.sHeader.saml);
		System.out.println(request.toString());
		DiscoveryResponseSoapEnvelope patientDiscoveryResponse = restTemplate.postForObject(serviceUrl + "/patientDiscovery", request, DiscoveryResponseSoapEnvelope.class);
		logger.info(patientDiscoveryResponse);
		logger.info("HIE sending patient discovery search query with SOAP...");
	}
	
	public void sendQueryRequest() throws Exception{
		RestTemplate restTemplate = new RestTemplate();
		QueryRequestSoapEnvelope request = XcpdUtils.generateQueryRequest(createSamlHeader());
		logger.info(request);
		QueryResponseSoapEnvelope queryResponse = restTemplate.postForObject(serviceUrl + "/queryRequest", request, QueryResponseSoapEnvelope.class);
		logger.info(queryResponse);
		logger.info("HIE sending query patient search with SOAP...");
	}
	
	public void sendDocumentSetRequest() throws Exception{
		RestTemplate restTemplate = new RestTemplate();
		RetrieveDocumentSetRequestSoapEnvelope request = XcpdUtils.generateDocumentRequest(createSamlHeader());
		logger.info(request);
		RetrieveDocumentSetResponseSoapEnvelope queryResponse = restTemplate.postForObject(serviceUrl + "/documentRequest", request, RetrieveDocumentSetResponseSoapEnvelope.class);
		logger.info(queryResponse);
		logger.info("HIE sending document set search query with SOAP...");
	}

	@Override
	public void run() {
		try {
			sendPatientSearchRequest();
			//sendQueryRequest();
			//sendDocumentSetRequest();
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
