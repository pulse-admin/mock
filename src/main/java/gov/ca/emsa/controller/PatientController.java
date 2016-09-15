package gov.ca.emsa.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.ca.emsa.Utils;
import gov.ca.emsa.domain.Patient;
import io.swagger.annotations.Api;

@RestController
@Api(value="/mock")
@RequestMapping("/mock")
public class PatientController {
	private static final Logger logger = LogManager.getLogger(PatientController.class);
	private static final String E_HEALTH_PATIENT_FILE_NAME = "ehealthPatients.xml";
	private static final String IHE_PATIENT_FILE_NAME = "ihePatients.xml";
	@Autowired private ResourceLoader resourceLoader;
	private DateTimeFormatter patientDateFormatter;
	private static final String PATIENT_DOB_FORMAT = "MM/dd/yyyy";
	
	private List<Patient> ehealthPatients = null;
	private List<Patient> ihePatients = null;
	
	public PatientController() {
		patientDateFormatter = DateTimeFormatter.ofPattern(PATIENT_DOB_FORMAT);
	}
	
	//note that the first name and last name search params must be a valid java regex
	@RequestMapping(value= "/ehealthexchange/patients", method = RequestMethod.POST, 
			produces="application/json; charset=utf-8")
	public List<Patient> getEHealthPatients(@RequestParam(value="givenName", required=false) String givenName,
			@RequestParam(value="familyName", required=false) String familyName,
			@RequestParam(value="dob", required = false) String dob,
			@RequestParam(value="gender", required=false) String gender,
			@RequestParam(value="samlMessage", required=false) String samlMessage) throws IOException, InterruptedException {
		
		loadPatients();
		
		try {	
			List<Patient> matchedPatients = searchPatients(givenName, familyName, dob, gender, ehealthPatients);
			long sleepMillis = (long)(Math.random()*60000);
			logger.info("Sleeping for " + (sleepMillis/1000) + " seconds");
			Thread.sleep(sleepMillis);
			return matchedPatients;
		} catch(InterruptedException inter) {
			logger.error("Interruped!", inter);
			throw inter;
		}
    }
	
	//note that the first name and last name search params must be a valid java regex
	@RequestMapping(value= "/ihe/patients", method = RequestMethod.POST, 
			produces="application/json; charset=utf-8")
	public List<Patient> getIHEPatients(@RequestParam(value="givenName", required=false) String givenName,
			@RequestParam(value="familyName", required=false) String familyName,
			@RequestParam(value="dob", required = false) String dob,
			@RequestParam(value="gender", required=false) String gender,
			@RequestParam(value="samlMessage", required=false) String samlMessage) throws IOException, InterruptedException {

		loadPatients();
		
    	try {
			List<Patient> matchedPatients = searchPatients(givenName, familyName, dob, gender, ihePatients);
			long sleepMillis = (long)(Math.random()*60000);
			logger.info("Sleeping for " + (sleepMillis/1000) + " seconds");
			Thread.sleep(sleepMillis);
			return matchedPatients;
		} catch(InterruptedException inter) {
			logger.error("Interruped!", inter);
			throw inter;
		} 
    }
		
	private void loadPatients() {
		//if(ehealthPatients == null || ehealthPatients.size() == 0) {
			Resource ehealthFile = resourceLoader.getResource("classpath:" + E_HEALTH_PATIENT_FILE_NAME);
			try {
				ehealthPatients = Utils.readPatients(ehealthFile.getInputStream());
			} catch(IOException ioEx) {
				logger.error("Could not read ehealthFile file " + ehealthFile.getFilename(), ioEx);
			}
		//}
		
		//if(ihePatients == null || ihePatients.size() == 0) {
			Resource iheFile = resourceLoader.getResource("classpath:" + IHE_PATIENT_FILE_NAME);
			try {
				ihePatients = Utils.readPatients(iheFile.getInputStream());
			} catch(IOException ioEx) {
				logger.error("Could not read iheFile file " + iheFile.getFilename(), ioEx);
			}
		//}
	}
	
	private List<Patient> searchPatients(String givenName, String familyName, String dob,
			String gender, List<Patient> toSearch) throws IOException {
		
		List<Patient> matchedPatients = new ArrayList<Patient>();
		//match against the passed in parameters
		for(Patient patient : ehealthPatients) {
			boolean givenNameMatch = true;
			if(!StringUtils.isEmpty(givenName)) {
				if(!StringUtils.isEmpty(patient.getGivenName()) &&
					!patient.getGivenName().equalsIgnoreCase(givenName)) {
					givenNameMatch = false;
				}
			}
			boolean familyNameMatch = true;
			if(!StringUtils.isEmpty(familyName)) {
				if(!StringUtils.isEmpty(patient.getFamilyName()) &&
					!patient.getFamilyName().equalsIgnoreCase(familyName)) {
					familyNameMatch = false;
				}
			}
			
			boolean dobMatch = true;
			if(!StringUtils.isEmpty(dob)) {
				ZonedDateTime dobDate = null;
				try {
					dobDate = ZonedDateTime.parse(dob, DateTimeFormatter.ISO_DATE_TIME);
					dobDate = dobDate.truncatedTo(ChronoUnit.DAYS);
				} catch(DateTimeParseException pex) {
					logger.error("Could not parse " + dob + " as a date in the format " + DateTimeFormatter.ISO_DATE_TIME);
					throw new IOException(pex.getMessage());
				} 
				
				ZonedDateTime patientDob = null;
				try {
					 LocalDate date = LocalDate.parse(patient.getDateOfBirth(), patientDateFormatter);
					 patientDob = date.atStartOfDay(ZoneId.of("GMT"));
					 patientDob = patientDob.truncatedTo(ChronoUnit.DAYS);
				} catch(DateTimeParseException pex) {
					logger.error("Could not parse " + patient.getDateOfBirth() + " as a date in the format " + PATIENT_DOB_FORMAT);
					throw new IOException(pex.getMessage());
				} 
				
				if(dobDate == null || patient.getDateOfBirth() == null ||
						!dobDate.isEqual(patientDob)) {
					dobMatch = false;
				}
			}
			
			boolean genderMatch = true;
			if(!StringUtils.isEmpty(gender)) {
				if(gender.toUpperCase().startsWith("M")) {
					gender = "M";
				} else if(gender.toUpperCase().startsWith("F")) {
					gender = "F";
				}
				if(!patient.getGender().equals(gender)) {
					genderMatch = false;
				}
			}
			
			if(givenNameMatch && familyNameMatch && dobMatch && genderMatch) {
				matchedPatients.add(patient);
			}
		}
		return matchedPatients;
	}
}
