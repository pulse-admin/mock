package gov.ca.emsa.service.impl;

import java.io.IOException;
import java.util.TimerTask;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class HIEPatientSearchService extends TimerTask {
	private static final Logger logger = LogManager.getLogger(HIEPatientSearchService.class);

	private String serviceUrl;
	private long intervalMillis;
	
	@Autowired private ResourceLoader resourceLoader;
	private static final String RETRIEVE_DOCUMENT_SET_RESOURCE_FILE_NAME = "ValidDocumentSetRequest.xml";
	private static final String DOCUMENT_QUERY_RESOURCE_FILE_NAME = "ValidDocumentQueryRequest.xml";
	private static final String PATIENT_DISCOVERY_RESOURCE_FILE_NAME = "ValidXcpdRequest.xml";
	int count = 1;
	
	/*public AssertionImpl createSamlHeader(){
		SAMLInput input = new SAMLInput();
		input.setAssertionId("12345678-1234-1234-012345678910");
		input.setStrNameID("CN=Alex G. Bell,O=1.22.333.4444,UID=abell");
		input.setSessionId("abcdedf1234567");

		HashMap<String, String> customAttributes = new HashMap<String,String>();
		//subject name
		customAttributes.put("urn:oasis:names:tc:xspa:1.0:subject:subject-id", "John");
		customAttributes.put("urn:oasis:names:tc:xspa:1.0:subject:organization", "Medical Clinic One");
		customAttributes.put("urn:oasis:names:tc:xspa:1.0:subject:organization-id", "medicalclinic.org");
		customAttributes.put("urn:nhin:names:saml:homeCommunityId", "urn:oid:2.16.840.1.113883.3.190");
		customAttributes.put("urn:oasis:names:tc:xacml:2.0:subject:role", "<Role xmlns=\"urn:hl7-org:v3\" xsi:type=\"CE\" code=\"46255001\"codeSystem=\"2.16.840.1.113883.6.96\" codeSystemName=\"SNOMED_CT\"displayName=\"Pharmacist\"/>");
		customAttributes.put("urn:oasis:names:tc:xspa:1.0:subject:purposeofuse", "<PurposeOfUse xmlns=\"urn:hl7-org:v3\" xsi:type=\"CE\" code=\"DISASTER\"codeSystem=\"2.16.840.1.113883.3.18.7.1\" codeSystemName=\"nhin-purpose\"displayName=\"Use and disclosures for disaster relief purposes\"/>");
		// patient identifier IDNumber^^^&OIDofAA&ISO
		customAttributes.put("urn:oasis:names:tc:xacml:2.0:resource:resource-id", "543797436^^^&amp;1.2.840.113619.6.197&amp;ISO");
		input.setAttributes(customAttributes);
		
		AssertionImpl samlMessage = null;

		try {
			samlMessage = samlGenerator.createSAML(input);
		} catch (MarshallingException e) {
			e.printStackTrace();
		}
		
		return samlMessage;
	}*/

	public void sendPatientSearchRequest() throws Exception{
		String request;
		RestTemplate restTemplate = new RestTemplate();
		try {
			Resource documentsFile = resourceLoader.getResource("classpath:" + PATIENT_DISCOVERY_RESOURCE_FILE_NAME);
			request = Resources.toString(documentsFile.getURL(), Charsets.UTF_8);
		} catch (IOException e) {
			logger.error(e);
			throw new HttpMessageNotWritableException("Could not read response file");
		}
		logger.info(serviceUrl + "/patientDiscovery");
		logger.info("HIE sending Patient Discovery search query from file " + PATIENT_DISCOVERY_RESOURCE_FILE_NAME);
		String patientDiscoveryResponse = restTemplate.postForObject(serviceUrl + "/patientDiscovery", request, String.class);
		//logger.info(patientDiscoveryResponse);
		
	}
	
	public void sendQueryRequest() throws Exception{
		String request;
		RestTemplate restTemplate = new RestTemplate();
		try {
			Resource documentsFile = resourceLoader.getResource("classpath:" + DOCUMENT_QUERY_RESOURCE_FILE_NAME);
			request = Resources.toString(documentsFile.getURL(), Charsets.UTF_8);
		} catch (IOException e) {
			logger.error(e);
			throw new HttpMessageNotWritableException("Could not read response file");
		}
		logger.info("HIE sending Document Query from file " + DOCUMENT_QUERY_RESOURCE_FILE_NAME);
		String queryResponse = restTemplate.postForObject(serviceUrl + "/documentQuery", request, String.class);
		//logger.info(queryResponse);
		
	}
	
	public void sendDocumentSetRequest() throws Exception{
		RestTemplate restTemplate = new RestTemplate();
		String request;
		try {
			Resource documentsFile = resourceLoader.getResource("classpath:" + RETRIEVE_DOCUMENT_SET_RESOURCE_FILE_NAME);
			request = Resources.toString(documentsFile.getURL(), Charsets.UTF_8);
		} catch (IOException e) {
			logger.error(e);
			throw new HttpMessageNotWritableException("Could not read response file");
		}
		logger.info("HIE sending Document Set Retreive query from file " + RETRIEVE_DOCUMENT_SET_RESOURCE_FILE_NAME);
		String queryResponse = restTemplate.postForObject(serviceUrl + "/retrieveDocumentSet", request, String.class);
		//logger.info(queryResponse);
		
	}

	@Override
	public void run() {
		try {
			if(count == 1){
				//sendPatientSearchRequest();
				count++;
			}else if(count == 2){
				sendQueryRequest();
				count++;
			}else if(count == 3){
				//sendDocumentSetRequest();
				count = 1;
			}
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
