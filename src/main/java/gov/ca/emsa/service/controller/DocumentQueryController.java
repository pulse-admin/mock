package gov.ca.emsa.service.controller;

import gov.ca.emsa.service.EHealthQueryConsumerService;

import java.io.IOException;

import javax.xml.soap.SOAPException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;

import org.opensaml.common.SAMLException;
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
public class DocumentQueryController {
	private static final Logger logger = LogManager.getLogger(DocumentQueryController.class);
	@Autowired private ResourceLoader resourceLoader;
	private static final String RESOURCE_FILE_NAME = "ValidDocumentQueryResponse.xml";

	@Autowired EHealthQueryConsumerService consumerService;

	@RequestMapping(value = "/documentQuery", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
	public String queryRequest(@RequestBody String request) {
		try{
			AdhocQueryRequest requestObj = consumerService.unMarshallDocumentQueryRequestObject(request);
		}catch(SAMLException e){
			return consumerService.createSOAPFault();
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
