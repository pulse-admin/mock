package gov.ca.emsa.service.controller;

import java.io.IOException;

import gov.ca.emsa.service.EHealthQueryConsumerService;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hl7.v3.PRPAIN201305UV02;
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
		PRPAIN201305UV02 requestObj = consumerService.unMarshallPatientDiscoveryRequestObject(request);

		try {
			Resource documentsFile = resourceLoader.getResource("classpath:" + RESOURCE_FILE_NAME);
			return Resources.toString(documentsFile.getURL(), Charsets.UTF_8);
		} catch (IOException e) {
			logger.error(e);
			throw new HttpMessageNotWritableException("Could not read response file");
		}
	}
}
