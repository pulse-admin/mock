package gov.ca.emsa;

import gov.ca.emsa.domain.Address;
import gov.ca.emsa.domain.Organization;
import gov.ca.emsa.domain.Organizations;
import gov.ca.emsa.domain.Patient;
import gov.ca.emsa.domain.Patients;

import java.io.InputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

public class Utils {
	
	public static List<Organization> readOrganizations(InputStream xmlFile){
		JAXBContext jaxbContext = null;
		List<Organization> organizations = null;
		try {
			jaxbContext = JAXBContext.newInstance(Organizations.class,Wrapper.class,Organization.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		Unmarshaller jaxbUnmarshaller = null;
		try {
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		try {
			organizations = unmarshal(jaxbUnmarshaller , Organization.class , xmlFile);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return organizations;
	}
	
	public static List<Patient> readPatients(InputStream xmlFile){
		JAXBContext jaxbContext = null;
		List<Patient> patients = null;
		try {
			jaxbContext = JAXBContext.newInstance(Patients.class,Wrapper.class,Patient.class, Address.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		Unmarshaller jaxbUnmarshaller = null;
		try {
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		try {
			patients = unmarshal(jaxbUnmarshaller , Patient.class , xmlFile);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		//I messed up when creating the test data - all the dates should have been yyyy-MM-dd
		DateTimeFormatter inFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		DateTimeFormatter outFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		for(Patient patient : patients) {
			TemporalAccessor dt = inFormatter.parse(patient.getDateOfBirth());
			patient.setDateOfBirth(outFormatter.format(dt));
		}
		return patients;
	}
	
	 private static <T> List<T> unmarshal(Unmarshaller unmarshaller,
	            Class<T> clazz, InputStream xmlFile) throws JAXBException {
		 
	        StreamSource xml = new StreamSource(xmlFile);
	        @SuppressWarnings("unchecked")
			Wrapper<T> wrapper = (Wrapper<T>) unmarshaller.unmarshal(xml,Wrapper.class).getValue();
	        return wrapper.getItems();
	    }

}
