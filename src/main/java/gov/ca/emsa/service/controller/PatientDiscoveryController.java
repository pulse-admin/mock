package gov.ca.emsa.service.controller;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hl7.v3.EnExplicitFamily;
import org.hl7.v3.EnExplicitGiven;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject1;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject2;
import org.hl7.v3.PRPAMT201310UV02Person;
import org.opensaml.common.SAMLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import gov.ca.emsa.pulse.common.domain.PatientSearch;
import gov.ca.emsa.pulse.common.soap.SOAPToJSONService;
import gov.ca.emsa.service.EHealthQueryConsumerService;

@RestController
public class PatientDiscoveryController {
	private static final Logger logger = LogManager.getLogger(PatientDiscoveryController.class);
	@Autowired private ResourceLoader resourceLoader;
	private static final String RESOURCE_FILE_NAME = "ValidXcpdResponse.xml";
	@Autowired EHealthQueryConsumerService consumerService;
	@Autowired SOAPToJSONService soapToJson;
	
	@Value("${minimumResponseSeconds}")
	private String minimumResponseSeconds;
	
	@Value("${maximumResponseSeconds}")
	private String maximumResponseSeconds;
	
	@RequestMapping(value = "/patientDiscovery", 
			method = RequestMethod.POST, 
			produces={"application/xml"} , 
			consumes ={"application/xml"})
	public String patientDiscovery(@RequestBody String request) throws InterruptedException {
		PRPAIN201305UV02 requestObj = null;
		try{
			requestObj = consumerService.unMarshallPatientDiscoveryRequestObject(request);
		}catch(SAMLException e){
			return consumerService.createSOAPFault();
		} catch (SOAPException e) {
			logger.error(e);
		}
		
		PatientSearch search = soapToJson.convertToPatientSearch(requestObj);
		String result = "";
		String defaultPatientDiscoveryResult = "";
		try {
			Resource pdFile = resourceLoader.getResource("classpath:" + RESOURCE_FILE_NAME);
			defaultPatientDiscoveryResult = Resources.toString(pdFile.getURL(), Charsets.UTF_8);
			
			//number of results we want to have; alter the data for that
			int numResults = getNumberOfResults();
			PRPAIN201306UV02 resultObj = consumerService.unMarshallPatientDiscoveryResponseObject(defaultPatientDiscoveryResult);
			List<PRPAIN201306UV02MFMIMT700711UV01Subject1> subjects = resultObj.getControlActProcess().getSubject();
			switch(numResults) {
			case 0:
				subjects.clear();
				break;
			case 1:
				subjects.remove(1);
				break;
			default:
					break;
			}
			
			//change the values in the return data to match the search parameters
			subjects = resultObj.getControlActProcess().getSubject();
			for(PRPAIN201306UV02MFMIMT700711UV01Subject1 subject : subjects) {
				PRPAIN201306UV02MFMIMT700711UV01Subject2 currSubject = subject.getRegistrationEvent().getSubject1();
				JAXBElement<PRPAMT201310UV02Person> patientPerson = currSubject.getPatient().getPatientPerson();
				List<PNExplicit> names = patientPerson.getValue().getName();
				for(PNExplicit name : names) {
					List<Serializable> nameParts = name.getContent();
					for(Serializable namePart : nameParts) {
						if(namePart instanceof JAXBElement<?>) {
							if(((JAXBElement<?>) namePart).getName().getLocalPart().equalsIgnoreCase("given")) {
								((JAXBElement<EnExplicitGiven>)namePart).getValue().setContent(search.getGivenName());
							} else if(((JAXBElement<?>) namePart).getName().getLocalPart().equalsIgnoreCase("family")) {
								((JAXBElement<EnExplicitFamily>)namePart).getValue().setContent(getLastName(search.getFamilyName()));
							}
						}
					}
				}
				
				if(search.getGender().startsWith("F")) {
					patientPerson.getValue().getAdministrativeGenderCode().setCode("F");
				} else if(search.getGender().startsWith("M")) {
					patientPerson.getValue().getAdministrativeGenderCode().setCode("M");
				} else {
					patientPerson.getValue().getAdministrativeGenderCode().setCode("UN");
				}
				
				LocalDate dob = LocalDate.parse(search.getDob(), DateTimeFormatter.ISO_DATE_TIME);
				patientPerson.getValue().getBirthTime().setValue(dob.format(DateTimeFormatter.BASIC_ISO_DATE));
			}
			result = consumerService.marshallPatientDiscoveryResponse(resultObj);
		} catch (IOException e) {
			logger.error(e);
			throw new HttpMessageNotWritableException("Could not read response file");
		} catch(SAMLException | SOAPException | JAXBException ex) {
			logger.error("Could not convert patient results file to XML object. Returning default XML.", ex);
			return defaultPatientDiscoveryResult;
		}
		
		try {	
			int minSeconds = new Integer(minimumResponseSeconds.trim()).intValue();
			int maxSeconds = new Integer(maximumResponseSeconds.trim()).intValue();
			int responseIntervalSeconds = ThreadLocalRandom.current().nextInt(minSeconds, maxSeconds + 1);
			logger.info("Sleeping for " + responseIntervalSeconds + " seconds");
			Thread.sleep(responseIntervalSeconds*1000);
			return result;
		} catch(InterruptedException inter) {
			logger.error("Interruped!", inter);
			throw inter;
		}		
	}
	
	private int getNumberOfResults() {
		long rand = Math.round(Math.random()*2);
		return (int)rand;
	}
	
	private String getLastName(String searchTerm) {
		long rand = Math.round(Math.random()*10);
		if(rand%2 == 0) {
			return searchTerm;
		}
		return searchTerm + "-Smith";
	}
}
