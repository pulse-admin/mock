package gov.ca.emsa.service.controller;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.opensaml.common.SAMLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import gov.ca.emsa.service.EHealthQueryConsumerService;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;

@RestController
public class DocumentQueryController {
	private static final Logger logger = LogManager.getLogger(DocumentQueryController.class);
	private static final String RESOURCE_FILE_NAME = "ValidDocumentQueryResponse.xml";
	private static final String ERROR_FILE_NAME = "ErrorQueryResponse.xml";

	private static final int MIN_DOCUMENTS = 1;
	private static final int MAX_DOCUMENTS = 2;
	
	@Autowired private ResourceLoader resourceLoader;
	@Autowired EHealthQueryConsumerService consumerService;

	@Value("${minimumResponseSeconds}")
	private String minimumResponseSeconds;
	
	@Value("${maximumResponseSeconds}")
	private String maximumResponseSeconds;
	
	@Value("${documentQueryPercentageFailing}")
	private int percentageFailing;
	
	@Value("${documentQueryPercentageError}")
	private int percentageError;
	
	@RequestMapping(value = "/documentQuery", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
	public String queryRequest(@RequestBody String request) throws InterruptedException, RandomFailingErrorException, SOAPException {
		logger.info("/documentQuery received request " + request);
		int randNum = 1 + (int)(Math.random() * ((100 - 1) + 1));
		if(randNum <= percentageFailing){
			throw new RandomFailingErrorException();
		}
		
		String result = "";
		randNum = 1 + (int)(Math.random() * ((100 - 1) + 1));
		if(randNum <= percentageError){
			logger.info("Returning error message.");
			//marshal the error message
			try {
			Resource errFile = resourceLoader.getResource("classpath:" + ERROR_FILE_NAME);
			result = Resources.toString(errFile.getURL(), Charsets.UTF_8);
			} catch(IOException ex) {
				logger.error("Could not open " + ERROR_FILE_NAME, ex);
			}
		} else {
			try{
				AdhocQueryRequest requestObj = consumerService.unMarshallDocumentQueryRequestObject(request);
				//logger.info(requestObj.getAdhocQuery().);
			}catch(SAMLException e){
				return consumerService.createSOAPFault();
			}
	
			int numDocsToReturn = ThreadLocalRandom.current().nextInt(MIN_DOCUMENTS, MAX_DOCUMENTS + 1);
			try {
				Resource documentsFile = resourceLoader.getResource("classpath:" + RESOURCE_FILE_NAME);
				AdhocQueryResponse response = consumerService.unMarshallDocumentQueryResponseObject(Resources.toString(documentsFile.getURL(), Charsets.UTF_8));
	
				//adjust based on num docs
				if(numDocsToReturn == 1) {
					RegistryObjectListType regList = response.getRegistryObjectList();
					regList.getIdentifiable().remove(1);	
				}
	
				result = consumerService.marshallDocumentQueryResponse(response);
			} catch (IOException |SAMLException | SOAPException | JAXBException e) {
				logger.error(e);
				throw new HttpMessageNotWritableException("Could not read response file");
			}
		}
		
		try {	
			int minSeconds = new Integer(minimumResponseSeconds.trim()).intValue();
			int maxSeconds = new Integer(maximumResponseSeconds.trim()).intValue();
			int responseIntervalSeconds = ThreadLocalRandom.current().nextInt(minSeconds, maxSeconds + 1);
			logger.info("/documentQuery is waiting for " + responseIntervalSeconds + " seconds to return " + result);
			Thread.sleep(responseIntervalSeconds*1000);
			return result;
		} catch(InterruptedException inter) {
			logger.error("Interruped!", inter);
			throw inter;
		}	
	}
}
