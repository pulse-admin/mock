package gov.ca.emsa.service.controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.xml.bind.JAXBException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.opensaml.common.SAMLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.io.Resources;
import com.sun.istack.ByteArrayDataSource;

import gov.ca.emsa.pulse.common.domain.DocumentIdentifier;
import gov.ca.emsa.service.EHealthQueryConsumerService;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

@RestController
public class DocumentSetRetrieveController {
	private static final Logger logger = LogManager.getLogger(DocumentSetRetrieveController.class);
	@Autowired private ResourceLoader resourceLoader;
	private static final String CCDA_RESOURCE_DIR = "ccdas";
	@Autowired EHealthQueryConsumerService consumerService;
	
	@Value("${minimumResponseSeconds}")
	private String minimumResponseSeconds;
	
	@Value("${maximumResponseSeconds}")
	private String maximumResponseSeconds;

	@RequestMapping(value = "/retrieveDocumentSet", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
	public String documentRequest(@RequestBody String request) throws InterruptedException  {
		try {
			RetrieveDocumentSetRequestType requestObj = consumerService.unMarshallDocumentSetRetrieveRequestObject(request);
			List<DocumentIdentifier> docIds = new ArrayList<DocumentIdentifier>();
			for(DocumentRequest currDocRequest : requestObj.getDocumentRequest()) {
				DocumentIdentifier docId = new DocumentIdentifier();
				docId.setRepositoryUniqueId(currDocRequest.getRepositoryUniqueId());
				docId.setHomeCommunityId(currDocRequest.getHomeCommunityId());
				docId.setDocumentUniqueId(currDocRequest.getDocumentUniqueId());
				docIds.add(docId);
			}
			RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
			RegistryResponseType registryResponse = new RegistryResponseType();
			registryResponse.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success");
			response.setRegistryResponse(registryResponse);
			
			for(DocumentIdentifier docId : docIds) {
				DocumentResponse docResponse = new DocumentResponse();
				docResponse.setRepositoryUniqueId(docId.getRepositoryUniqueId());
				docResponse.setHomeCommunityId(docId.getHomeCommunityId());
				docResponse.setDocumentUniqueId(docId.getDocumentUniqueId());
				docResponse.setMimeType("text/xml");
				
				File documentFile = getRandomFileInDir(CCDA_RESOURCE_DIR);
				Resource documentResource = resourceLoader.getResource("classpath:" + CCDA_RESOURCE_DIR + File.separator + documentFile.getName());
				String docStr = Resources.toString(documentResource.getURL(), Charset.forName("UTF-8"));
				String binaryDocStr = base64EncodeMessage(docStr);
				DataSource ds = new ByteArrayDataSource(binaryDocStr.getBytes(), "text/xml; charset=UTF-8");
			    DataHandler dh = new DataHandler(ds);
				docResponse.setDocument(dh);
				response.getDocumentResponse().add(docResponse);
			}
			
			try {	
				int minSeconds = new Integer(minimumResponseSeconds.trim()).intValue();
				int maxSeconds = new Integer(maximumResponseSeconds.trim()).intValue();
				int responseIntervalSeconds = ThreadLocalRandom.current().nextInt(minSeconds, maxSeconds + 1);
				logger.info("Sleeping for " + responseIntervalSeconds + " seconds");
				Thread.sleep(responseIntervalSeconds*1000);
				return consumerService.marshallDocumentSetResponseType(response);
			} catch(InterruptedException inter) {
				logger.error("Interruped!", inter);
				throw inter;
			}	
		} catch (IOException | SAMLException | JAXBException e) {
			logger.error(e);
			return consumerService.createSOAPFault();
		}		
	}
	
	private File getRandomFileInDir(String dir) throws IOException {
		File result = null;
		
		Resource ccdaResource = resourceLoader.getResource("classpath:" + dir);
		File ccdaDir = ccdaResource.getFile();
		if(ccdaDir.exists() && ccdaDir.isDirectory()) {
			File[] ccdas = ccdaDir.listFiles();
			if(ccdas != null && ccdas.length > 0) {
				int fileIndex = (int)(Math.floor(Math.random()*ccdas.length));
				result = ccdas[fileIndex];
			} else {
				logger.error("Could not find any ccdas in " + ccdaDir.getAbsolutePath());
			}
		}
		return result;
	}
	
	 private String base64EncodeMessage(String rawMessage){
		 byte[] bytes = rawMessage.getBytes(Charset.forName("UTF-8"));
	     String encoded = Base64.getEncoder().encodeToString(bytes);
	     return encoded;
	 }
}
