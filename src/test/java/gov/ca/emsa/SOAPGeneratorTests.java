package gov.ca.emsa;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.validation.Validator;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

@Service
@Configuration
public class SOAPGeneratorTests {
	
	public static boolean validateXML(SOAPMessage soapMessage, Resource schemaResource) throws ParserConfigurationException, SAXException, IOException, SOAPException{
		
		// create a SchemaFactory capable of understanding WXS schemas
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		
		// load a WXS schema, represented by a Schema instance
		Schema schema = factory.newSchema(schemaResource.getFile());

		// create a Validator instance, which can be used to validate an instance document
		Validator validator = schema.newValidator();
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		soapMessage.writeTo(stream);
		String message = new String(stream.toByteArray(), "utf-8");
		// validate the DOM tree
		try {
			validator.validate(new StreamSource(new StringReader(message)));
		} catch (SAXException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Test
	public void SOAPQueryByParameterMessageTest() throws SOAPException, ParserConfigurationException, SAXException, IOException{
		SOAPGenerator sg = new SOAPGenerator();
		sg.addHeader();
		sg.addQueryForPatient("Brian","Lindsey");
		Resource resource = new ClassPathResource("PRPA_MT201306UV02.xsd");
		assertTrue(validateXML(sg.getMessage(),resource));
	}
	
	@Test
	public void SOAPQueryOfPatientListMessageTest() throws SOAPException, ParserConfigurationException, SAXException, IOException{
		SOAPGenerator sg = new SOAPGenerator();
		sg.addHeader();
		sg.addQueryOfPatientList(new ArrayList<String>());
		Resource resource = new ClassPathResource("soapRequest.xsd");
		assertTrue(validateXML(sg.getMessage(),resource));
	}
	
	@Test
	public void SOAPQueryOfDocumentMessageTest() throws SOAPException, ParserConfigurationException, SAXException, IOException{
		SOAPGenerator sg = new SOAPGenerator();
		sg.addHeader();
		sg.addQueryOfDocumentList(new ArrayList<String>());
		Resource resource = new ClassPathResource("soapRequest.xsd");
		assertTrue(validateXML(sg.getMessage(),resource));
	}
}
