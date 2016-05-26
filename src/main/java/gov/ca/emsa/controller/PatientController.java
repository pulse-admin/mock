package gov.ca.emsa.controller;

import gov.ca.emsa.domain.Patient;
import io.swagger.annotations.Api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
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

@RestController
@Api(value="/mock")
@RequestMapping("/mock")
public class PatientController {
	private static final Logger logger = LogManager.getLogger(PatientController.class);
	private static final String PATIENT_FILE_NAME = "eHealthpatients.csv";
	private static final String DATE_FORMAT = "yyyyMMdd";
	
	@Autowired private ResourceLoader resourceLoader;
	private DateFormat dateFormatter;
	
	public PatientController() {
		dateFormatter = new SimpleDateFormat(DATE_FORMAT);
	}
	
	//note that the first name and last name search params must be a valid java regex
	@RequestMapping(value= "/ehealthexchange/patients", method = RequestMethod.GET, 
			produces="application/json; charset=utf-8")
	public List<Patient> getPatients(@RequestParam(value="firstName", required=false) String firstName,
			@RequestParam(value="lastName", required=false) String lastName) throws IOException {
		
		Resource patientsFile = resourceLoader.getResource("classpath:" + PATIENT_FILE_NAME);
		
    	//load all patients from the file
		BufferedReader reader = null;
		CSVParser parser = null;
		try {
			reader = new BufferedReader(new InputStreamReader(patientsFile.getInputStream()));
			parser = new CSVParser(reader, CSVFormat.EXCEL);
			
			List<CSVRecord> records = parser.getRecords();
			if(records.size() <= 1) {
				throw new IOException("The file appears to have a header line with no other information. Please make sure there are at least two rows in the CSV file.");
			}
			
			List<Patient> allPatients = new ArrayList<Patient>();
			for(CSVRecord record : records) {
				String colValue = record.get(0).toString().trim();
				if(!StringUtils.isEmpty(colValue) && !"ID".equals(colValue)) {
					Patient patient = new Patient();
					patient.setId(colValue);
					patient.setFirstName(record.get(1).toString().trim());
					patient.setLastName(record.get(2).toString().trim());
					String dateStr = record.get(3).toString().trim();
					if(!StringUtils.isEmpty(dateStr)) {
						try {
						Date dateOfBirth = dateFormatter.parse(dateStr);
						patient.setDateOfBirth(dateOfBirth);
						} catch(ParseException pex) {
							logger.error("Could not parse " + dateStr + " as a date in the format " + DATE_FORMAT);
							throw new IOException(pex.getMessage());
						}
					}
					patient.setGender(record.get(4).toString().trim());
					patient.setPhoneNumber(record.get(5).toString().trim());
					patient.setAddressLine1(record.get(6).toString().trim());
					patient.setAddressLine2(record.get(7).toString().trim());
					patient.setCity(record.get(8).toString().trim());
					patient.setState(record.get(9).toString().trim());
					patient.setZipcode(record.get(10).toString().trim());
					patient.setSsn(record.get(11).toString().trim());
					allPatients.add(patient);
				}
			}
			
			//match against the passed in parameters
			List<Patient> matchedPatients = new ArrayList<Patient>();
			for(Patient patient : allPatients) {
				boolean firstNameMatch = true;
				if(!StringUtils.isEmpty(firstName)) {
					if(!StringUtils.isEmpty(patient.getFirstName()) &&
						!patient.getFirstName().matches(firstName)) {
						firstNameMatch = false;
					}
				}
				boolean lastNameMatch = true;
				if(!StringUtils.isEmpty(lastName)) {
					if(!StringUtils.isEmpty(patient.getLastName()) &&
						!patient.getLastName().matches(lastName)) {
						lastNameMatch = false;
					}
				}
				
				if(firstNameMatch && lastNameMatch) {
					matchedPatients.add(patient);
				}
			}
			return matchedPatients;
		} catch(IOException ioEx) {
			logger.error("Could not get input stream for uploaded file " + PATIENT_FILE_NAME);			
			throw ioEx;
		} finally {
			 try { parser.close(); } catch(Exception ignore) {}
			try { reader.close(); } catch(Exception ignore) {}
		}
    }
}
