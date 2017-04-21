package gov.ca.emsa.service.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hl7.v3.ADExplicit;
import org.hl7.v3.EnExplicitFamily;
import org.hl7.v3.EnExplicitPrefix;
import org.hl7.v3.EnExplicitSuffix;
import org.hl7.v3.II;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject1;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject2;
import org.hl7.v3.PRPAMT201306UV02PatientAddress;
import org.hl7.v3.PRPAMT201310UV02OtherIDs;
import org.hl7.v3.PRPAMT201310UV02Patient;
import org.hl7.v3.PRPAMT201310UV02Person;
import org.hl7.v3.TELExplicit;
import org.opensaml.common.SAMLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import gov.ca.emsa.pulse.common.domain.PatientSearch;
import gov.ca.emsa.pulse.common.domain.PatientSearchAddress;
import gov.ca.emsa.pulse.common.soap.SOAPToJSONService;
import gov.ca.emsa.service.EHealthQueryConsumerService;

import gov.ca.emsa.Util;
@RestController
public class PatientDiscoveryController {
	private static final Logger logger = LogManager.getLogger(PatientDiscoveryController.class);
	@Autowired private ResourceLoader resourceLoader;
	private static final String RESOURCE_FILE_NAME = "ValidXcpdResponse.xml";
	private static final String ERROR_FILE_NAME = "ErrorQueryResponse.xml";

	@Autowired EHealthQueryConsumerService consumerService;
	@Autowired SOAPToJSONService soapToJson;
	Util util;
	
	@Value("${assigningAuthority}")
	private String assigningAuthority;
	private List<String> patientIds;
	private static final String[] cities = {"Austin", "Springfield", "Baltimore"};
	private static final String[] states = { "TX", "IL", "MD" };
	private static final String[] zipcodes = { "11330", "21228", "45678"};
	private static final String[] addrLines = {"510 Hunters Lane", "1110 Roland Ave", "4432A Snowflake Ave."};
	
	@Value("${minimumResponseSeconds}")
	private String minimumResponseSeconds;
	
	@Value("${maximumResponseSeconds}")
	private String maximumResponseSeconds;
	
	@Value("${patientDiscoveryPercentageFailing}")
	private int percentageFailing;
	
	@Value("${patientDiscoveryPercentageError}")
	private int percentageError;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/patientDiscovery", 
			method = RequestMethod.POST, 
			produces={"application/xml"} , 
			consumes ={"application/xml"})
	public String patientDiscovery(@RequestBody String request) throws InterruptedException, RandomFailingErrorException {
		logger.info("/patientDiscovery received a request");
		System.out.println("Request:" + request);
		int randNum = 1 + (int)(Math.random() * ((100 - 1) + 1));
		if(randNum <= percentageFailing){
			throw new RandomFailingErrorException();
		}
		
		String result = "";
		
		randNum = 1 + (int)(Math.random() * ((100 - 1) + 1));
		if(randNum <= percentageError) {
			logger.info("Returning error message.");
			//marshal the error message
			try {
				Resource errFile = resourceLoader.getResource("classpath:" + ERROR_FILE_NAME);
				result = Resources.toString(errFile.getURL(), Charsets.UTF_8);
			} catch(IOException ex) {
				logger.error("Could not open " + ERROR_FILE_NAME, ex);
			}
		} else {
			PRPAIN201305UV02 requestObj = null;
			try{
				requestObj = consumerService.unMarshallPatientDiscoveryRequestObject(request);
			}catch(SAMLException e){
				return consumerService.createSOAPFault();
			} catch (SOAPException e) {
				logger.error(e);
			}
			
			PatientSearch search = soapToJson.convertToPatientSearch(requestObj);
			String patientDiscoveryResult = "";
			try {
				Resource pdFile = resourceLoader.getResource("classpath:" + RESOURCE_FILE_NAME);
				patientDiscoveryResult = Resources.toString(pdFile.getURL(), Charsets.UTF_8);
				
				//number of results we want to have; alter the data for that
				int numResults = getNumberOfResults();
				PRPAIN201306UV02 resultObj = consumerService.unMarshallPatientDiscoveryResponseObject(patientDiscoveryResult);
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
				
				patientIds = util.getStatuses();
				//change the values in the return data to match the search parameters
				subjects = resultObj.getControlActProcess().getSubject();
				for(PRPAIN201306UV02MFMIMT700711UV01Subject1 subject : subjects) {
					PRPAIN201306UV02MFMIMT700711UV01Subject2 currSubject = subject.getRegistrationEvent().getSubject1();
					PRPAMT201310UV02Patient patient = currSubject.getPatient();
					int randNum1 = 0 + (int)(Math.random() * (((patientIds.size()-1) - 0) + 1));
					II patientIdElement = new II();
					patientIdElement.setRoot(assigningAuthority);
					patientIdElement.setExtension(patientIds.get(randNum1));
					patient.getId().add(patientIdElement);
					JAXBElement<PRPAMT201310UV02Person> patientPerson = currSubject.getPatient().getPatientPerson();
					List<PNExplicit> names = patientPerson.getValue().getName();
					for(PNExplicit name : names) {
						List<Serializable> nameParts = name.getContent();
						for(Serializable namePart : nameParts) {
							if(namePart instanceof JAXBElement<?>) {
								if(((JAXBElement<?>) namePart).getName().getLocalPart().equalsIgnoreCase("family")) {
									((JAXBElement<EnExplicitFamily>)namePart).getValue().setContent(getLastName(search.getPatientNames().get(0).getFamilyName()));
								}else if(((JAXBElement<?>) namePart).getName().getLocalPart().equalsIgnoreCase("prefix")){
									((JAXBElement<EnExplicitPrefix>)namePart).getValue().setContent(search.getPatientNames().get(0).getPrefix());
								}else if(((JAXBElement<?>) namePart).getName().getLocalPart().equalsIgnoreCase("suffix")){
									((JAXBElement<EnExplicitSuffix>)namePart).getValue().setContent(search.getPatientNames().get(0).getSuffix());
								}
							}
						}
						for(String given : search.getPatientNames().get(0).getGivenName()){
							JAXBElement<String> givenName = new JAXBElement<String>(new QName("given"), String.class, given);
							nameParts.add(givenName);
						}
					}
					
					if(search.getGender().startsWith("F")) {
						patientPerson.getValue().getAdministrativeGenderCode().setCode("F");
					} else if(search.getGender().startsWith("M")) {
						patientPerson.getValue().getAdministrativeGenderCode().setCode("M");
					} else {
						patientPerson.getValue().getAdministrativeGenderCode().setCode("UN");
					}
					
					patientPerson.getValue().getBirthTime().setValue(getDob(search.getDob()));
					
					if(!StringUtils.isEmpty(search.getTelephone())){
						TELExplicit tel = new TELExplicit();
						tel.setValue("tel:+1-" + search.getTelephone());
						patientPerson.getValue().getTelecom().add(tel);
					}
					if(!StringUtils.isEmpty(search.getSsn())){
						List<PRPAMT201310UV02OtherIDs> otherIds = patientPerson.getValue().getAsOtherIDs();
						for(PRPAMT201310UV02OtherIDs otherId : otherIds) {
							List<String> classCodes = otherId.getClassCode();
							for(String classCode : classCodes) {
								if(classCode.equalsIgnoreCase("CIT")) {
									List<II> citIds = otherId.getId();
									for(II citId : citIds) {
										if(citId.getRoot().equals("2.16.840.1.113883.4.1")) {
											citId.setExtension(search.getSsn());
										}
									}
								}
							}
						}
					}
					
					Random random = new Random();
					PRPAMT201306UV02PatientAddress patientAddress = new PRPAMT201306UV02PatientAddress();
					if(search.getAddresses() != null){
						patientPerson.getValue().getAddr().clear();
						for(PatientSearchAddress patientSearchAddress : search.getAddresses()){
							ADExplicit addr = new ADExplicit();
							String state = patientSearchAddress.getState();
							if(StringUtils.isEmpty(state)) {
								state = states[Math.round(random.nextInt(states.length-1))];
							}
							addr.getContent().add(new JAXBElement<String>(new QName("state"), String.class, state));
							String city = patientSearchAddress.getCity();
							if(StringUtils.isEmpty(city)) {
								city = cities[Math.round(random.nextInt(cities.length-1))];
							}
							addr.getContent().add(new JAXBElement<String>(new QName("city"), String.class, city));
							String zip = patientSearchAddress.getZipcode();
							if(StringUtils.isEmpty(zip)) {
								zip = zipcodes[Math.round(random.nextInt(zipcodes.length-1))];
							}
							addr.getContent().add(new JAXBElement<String>(new QName("postalCode"), String.class, zip));
	
							if(patientSearchAddress.getLines() != null && patientSearchAddress.getLines().size() > 0) {
								for(String line : patientSearchAddress.getLines()){
									if(StringUtils.isEmpty(line)) {
										line = addrLines[Math.round(random.nextInt(addrLines.length-1))];
									}
									addr.getContent().add(new JAXBElement<String>(new QName("streetAddressLine"), String.class, line));
								}
							} else {
								String line = addrLines[Math.round(random.nextInt(addrLines.length-1))];
								addr.getContent().add(new JAXBElement<String>(new QName("streetAddressLine"), String.class, line));
							}
							patientAddress.getValue().add(addr);
							patientPerson.getValue().getAddr().add(addr);
						}
					}
					
				}
				result = consumerService.marshallPatientDiscoveryResponse(resultObj);
			} catch (IOException e) {
				logger.error(e);
				throw new HttpMessageNotWritableException("Could not read response file");
			} catch(SAMLException | SOAPException | JAXBException ex) {
				logger.error("Could not convert patient results file to XML object. Returning default XML.", ex);
				return patientDiscoveryResult;
			}
		}
		
		try {	
			int minSeconds = new Integer(minimumResponseSeconds.trim()).intValue();
			int maxSeconds = new Integer(maximumResponseSeconds.trim()).intValue();
			int responseIntervalSeconds = ThreadLocalRandom.current().nextInt(minSeconds, maxSeconds + 1);
			logger.info("/patientDiscovery is waiting for " + responseIntervalSeconds + " seconds to return a successful response.");
			Thread.sleep(responseIntervalSeconds*1000);
			logger.info("/patientDiscovery is returning a successful response.");
			logger.info("/patientDsicovery result is: " + result);
			return result;
		} catch(InterruptedException inter) {
			logger.error("Interruped!", inter);
			throw inter;
		}		
	}
	
	private String getDob(String dob){
		int randNum = 1 + (int)(Math.random() * ((3 - 1) + 1));
		String randMonth = String.valueOf(1 + (int)(Math.random() * ((12 - 1) + 1)));
		if(Integer.parseInt(randMonth) < 10)
			randMonth = "0" + randMonth;
		String randDay = String.valueOf(1 + (int)(Math.random() * ((30 - 1) + 1)));
		if(Integer.parseInt(randDay) < 10)
			randDay = "0" + randDay;
		String randHour = String.valueOf(1 + (int)(Math.random() * ((24 - 1) + 1)));
		if(Integer.parseInt(randHour) < 10)
			randHour = "0" + randHour;
		String randMinute = String.valueOf(1 + (int)(Math.random() * ((60 - 1) + 1)));
		if(Integer.parseInt(randMinute) < 10)
			randMinute = "0" + randMinute;
		if(dob.length() == 4){
			if(randNum == 1)
				return dob + randMonth;
			else
				return dob + randMonth + randDay;
		}else if(dob.length() == 6){
			if(randNum == 1)
				return dob + randDay;
			else if (randNum == 2)
				return dob + randDay + randHour;
			else
				return dob;
		}else if(dob.length() == 8){
			if(randNum == 1)
				return dob + randHour;
			else if (randNum == 2)
				return dob + randHour + randMinute;
			else
				return dob;
		}else if(dob.length() == 10){
			if(randNum == 1)
				return dob + randMinute;
			else
				return dob;
		}
		return dob;
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
