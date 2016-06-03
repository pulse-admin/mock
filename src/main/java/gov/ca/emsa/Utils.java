package gov.ca.emsa;

import gov.ca.emsa.domain.Organization;
import gov.ca.emsa.domain.Organizations;

import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

public class Utils {
	
	public static List<Organization> readOrganizations(String file){
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
			organizations = unmarshal(jaxbUnmarshaller , Organization.class , file);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return organizations;
	}
	
	 private static <T> List<T> unmarshal(Unmarshaller unmarshaller,
	            Class<T> clazz, String xmlLocation) throws JAXBException {
	        StreamSource xml = new StreamSource(xmlLocation);
	        Wrapper<T> wrapper = (Wrapper<T>) unmarshaller.unmarshal(xml,Wrapper.class).getValue();
	        return wrapper.getItems();
	    }

}
