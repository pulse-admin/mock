package gov.ca.emsa.service.controller;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;

import gov.ca.emsa.service.EHealthQueryConsumerService;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hl7.v3.PRPAIN201305UV02;
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

@RestController
public class PatientDiscoveryController {
	private static final Logger logger = LogManager.getLogger(PatientDiscoveryController.class);
	@Autowired private ResourceLoader resourceLoader;
	private static final String RESOURCE_FILE_NAME = "ValidXcpdResponse.xml";
	
	@Autowired EHealthQueryConsumerService consumerService;
	
	@RequestMapping(value = "/patientDiscovery", method = RequestMethod.POST, produces={"application/xml"} , consumes ={"application/xml"})
	public String patientDiscovery(@RequestBody String request){
		try{
			PRPAIN201305UV02 requestObj = consumerService.unMarshallPatientDiscoveryRequestObject(request);
		}catch(SAMLException e){
			return consumerService.createSOAPFault();
		} catch (SOAPException e) {
			logger.error(e);
		}
		try {
			Resource documentsFile = resourceLoader.getResource("classpath:" + RESOURCE_FILE_NAME);
			return Resources.toString(documentsFile.getURL(), Charsets.UTF_8);
		} catch (IOException e) {
			logger.error(e);
			throw new HttpMessageNotWritableException("Could not read response file");
		}
	}
}
