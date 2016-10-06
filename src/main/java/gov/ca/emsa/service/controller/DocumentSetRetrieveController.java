package gov.ca.emsa.service.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.xml.bind.JAXBException;

import gov.ca.emsa.pulse.common.domain.DocumentIdentifier;
import gov.ca.emsa.service.EHealthQueryConsumerService;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.IOUtils;
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
public class DocumentSetRetrieveController {
	private static final Logger logger = LogManager.getLogger(DocumentSetRetrieveController.class);
	@Autowired private ResourceLoader resourceLoader;
	private static final String CCDA_RESOURCE_DIR = "ccdas";

	@Autowired EHealthQueryConsumerService consumerService;

	@RequestMapping(value = "/retrieveDocumentSet", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
	public String documentRequest(@RequestBody String request) {
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
				DataSource ds = new FileDataSource(documentResource.getFile());
			    DataHandler dh = new DataHandler(ds);
				docResponse.setDocument(dh);
				response.getDocumentResponse().add(docResponse);
			}
			return consumerService.marshallDocumentSetResponseType(response);
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
}
