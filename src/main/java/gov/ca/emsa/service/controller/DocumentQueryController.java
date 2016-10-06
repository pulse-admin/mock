package gov.ca.emsa.service.controller;

import gov.ca.emsa.pulse.common.domain.Document;
import gov.ca.emsa.pulse.common.domain.DocumentIdentifier;
import gov.ca.emsa.service.EHealthQueryConsumerService;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.LocalizedStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;

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
	private static final String RESOURCE_FILE_NAME = "ValidDocumentQueryResponse.xml";
	private static final String CCDA_RESOURCE_DIR = "ccdas";
	private static final int MIN_DOCUMENTS = 1;
	private static final int MAX_DOCUMENTS = 2;
	
	@Autowired private ResourceLoader resourceLoader;
	@Autowired EHealthQueryConsumerService consumerService;

	@RequestMapping(value = "/documentQuery", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
	public String queryRequest(@RequestBody String request) {
		try{
			AdhocQueryRequest requestObj = consumerService.unMarshallDocumentQueryRequestObject(request);
		}catch(SAMLException e){
			return consumerService.createSOAPFault();
		}

		int numDocsToReturn = ThreadLocalRandom.current().nextInt(MIN_DOCUMENTS, MAX_DOCUMENTS + 1);
		String result = "";
		try {
			Resource documentsFile = resourceLoader.getResource("classpath:" + RESOURCE_FILE_NAME);
			AdhocQueryResponse response = consumerService.unMarshallDocumentQueryResponseObject(Resources.toString(documentsFile.getURL(), Charsets.UTF_8));

			//adjust based on num docs
			if(numDocsToReturn == 1) {
				RegistryObjectListType regList = response.getRegistryObjectList();
				regList.getIdentifiable().remove(1);	
			}
			
			//adjust the sizes of the files to be the largest out of all the files in the ccda dir
			long largestSize = getLargestFileSizeInDir(CCDA_RESOURCE_DIR);
			RegistryObjectListType regList = response.getRegistryObjectList();
			List<JAXBElement<? extends IdentifiableType>> idTypes = regList.getIdentifiable();
			for(Object obj : idTypes) {
				if(obj instanceof JAXBElement<?>) {
					JAXBElement<?> jaxbObj = (JAXBElement<?>)obj;
					if(jaxbObj != null && jaxbObj.getValue() != null && jaxbObj.getValue() instanceof ExtrinsicObjectType) {
						ExtrinsicObjectType extObj = (ExtrinsicObjectType)jaxbObj.getValue();
						List<SlotType1> slots = extObj.getSlot();
						for(SlotType1 slot : slots) {
							if(slot.getName().equalsIgnoreCase("size")) {
								ValueListType values = slot.getValueList();
								values.getValue().set(0, largestSize+"");
							}
						}
					}
				}
			}

			result = consumerService.marshallDocumentQueryResponse(response);
		} catch (IOException |SAMLException | SOAPException | JAXBException e) {
			logger.error(e);
			throw new HttpMessageNotWritableException("Could not read response file");
		}
		
		return result;
	}
	
	private long getLargestFileSizeInDir(String dir) throws IOException {
		long largest = 0;
		
		Resource ccdaResource = resourceLoader.getResource("classpath:" + dir);
		File ccdaDir = ccdaResource.getFile();
		if(ccdaDir.exists() && ccdaDir.isDirectory()) {
			File[] ccdas = ccdaDir.listFiles();
			for(int i = 0; i < ccdas.length; i++) {
				if(ccdas[i].length() > largest) {
					largest = ccdas[i].length();
				}
			}
		}
		return largest;
	}
}
