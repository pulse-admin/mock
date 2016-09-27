package gov.ca.emsa.service.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

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
			
			//change the values in the return data to match the search parameters
			PRPAIN201306UV02 resultObj = consumerService.unMarshallPatientDiscoveryResponseObject(defaultPatientDiscoveryResult);
			List<PRPAIN201306UV02MFMIMT700711UV01Subject1> subjects = resultObj.getControlActProcess().getSubject();
			if(subjects != null && subjects.size() > 0) {
				PRPAIN201306UV02MFMIMT700711UV01Subject1 subject = subjects.get(0);
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
								((JAXBElement<EnExplicitFamily>)namePart).getValue().setContent(search.getFamilyName());
							}
						}
					}
				}
				patientPerson.getValue().getAdministrativeGenderCode().setCode(search.getGender());
				
				//trim off the "T" part of the date
				String dob = search.getDob();
				if(dob.contains("T")) {
					int timeIndex = dob.indexOf("T");
					dob = dob.substring(0, timeIndex);
				}
				patientPerson.getValue().getBirthTime().setValue(dob);
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
			long sleepMillis = (long)(Math.random()*60000);
			logger.info("Sleeping for " + (sleepMillis/1000) + " seconds");
			Thread.sleep(sleepMillis);
			return result;
		} catch(InterruptedException inter) {
			logger.error("Interruped!", inter);
			throw inter;
		}		
	}
}
