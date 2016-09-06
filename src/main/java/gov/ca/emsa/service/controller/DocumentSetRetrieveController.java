package gov.ca.emsa.service.controller;

import java.io.IOException;

import gov.ca.emsa.service.EHealthQueryConsumerService;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

@RestController
public class DocumentSetRetrieveController {
	private static final Logger logger = LogManager.getLogger(DocumentSetRetrieveController.class);
	@Autowired private ResourceLoader resourceLoader;
	private static final String RESOURCE_FILE_NAME = "ValidDocumentSetRetrieveResponse.xml";
	
	@Autowired EHealthQueryConsumerService consumerService;
	
	@RequestMapping(value = "/retrieveDocumentSet", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
	public String documentRequest(@RequestBody String request) {
		RetrieveDocumentSetRequestType requestObj = consumerService.unMarshallDocumentSetRetrieveRequestObject(request);
		
		try {
			Resource documentsFile = resourceLoader.getResource("classpath:" + RESOURCE_FILE_NAME);
			return Resources.toString(documentsFile.getURL(), Charsets.UTF_8);
		} catch (IOException e) {
			logger.error(e);
			throw new HttpMessageNotWritableException("Could not read response file");
		}
	}
}
