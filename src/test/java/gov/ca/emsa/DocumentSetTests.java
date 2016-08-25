package gov.ca.emsa;

import static org.junit.Assert.assertNotNull;
import gov.ca.emsa.xcpd.aqr.AdhocQuery;
import gov.ca.emsa.xcpd.aqr.Slot;
import gov.ca.emsa.xcpd.rds.DocumentRequest;
import gov.ca.emsa.xcpd.rds.DocumentResponse;
import gov.ca.emsa.xcpd.soap.QueryRequestSoapEnvelope;
import gov.ca.emsa.xcpd.soap.RetrieveDocumentSetRequestSoapEnvelope;
import gov.ca.emsa.xcpd.soap.RetrieveDocumentSetResponseSoapEnvelope;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

public class DocumentSetTests {
	public static final String VALID_DOCUMENT_REQUEST_FILE = "ValidDocumentSetRequest.xml";
	public static final String VALID_DOCUMENT_RESPONSE_FILE = "ValidDocumentSetResponse.xml";
	
	@Test
	public void mapRequestToObjectBody() throws JAXBException{
		Unmarshaller unmarshaller = JAXBContext.newInstance(RetrieveDocumentSetRequestSoapEnvelope.class).createUnmarshaller();
		ClassLoader classLoader = getClass().getClassLoader();
		RetrieveDocumentSetRequestSoapEnvelope qr = (RetrieveDocumentSetRequestSoapEnvelope)
				unmarshaller.unmarshal(classLoader.getResource(VALID_DOCUMENT_REQUEST_FILE));
		
		//check all SOAP header elements are there
		assertNotNull(qr.header);
		assertNotNull(qr.header.action);
		assertNotNull(qr.header.action.mustUnderstand);
		assertNotNull(qr.header.messageId);
		assertNotNull(qr.header.replyTo.address);
		
		assertNotNull(qr.body);
		assertNotNull(qr.body.documentSetRequest);
		assertNotNull(qr.body.documentSetRequest.documentRequest);
		for(DocumentRequest dr : qr.body.documentSetRequest.documentRequest){
			assertNotNull(dr.documentUniqueId);
			assertNotNull(dr.repositoryUniqueId);
		}
	}
	
	@Test
	public void mapResponseToObjectBody() throws JAXBException{
		Unmarshaller unmarshaller = JAXBContext.newInstance(RetrieveDocumentSetResponseSoapEnvelope.class).createUnmarshaller();
		ClassLoader classLoader = getClass().getClassLoader();
		RetrieveDocumentSetResponseSoapEnvelope qr = (RetrieveDocumentSetResponseSoapEnvelope)
				unmarshaller.unmarshal(classLoader.getResource(VALID_DOCUMENT_RESPONSE_FILE));
		
		//check all SOAP header elements are there
		assertNotNull(qr.header);
		assertNotNull(qr.header.action);
		assertNotNull(qr.header.action.mustUnderstand);
		assertNotNull(qr.header.messageId);
		assertNotNull(qr.header.relatesTo);
		
		assertNotNull(qr.body);
		assertNotNull(qr.body.retrieveDocumentSetResponse);
		assertNotNull(qr.body.retrieveDocumentSetResponse.registryReponse);
		for(DocumentResponse dr : qr.body.retrieveDocumentSetResponse.documentResponse){
			assertNotNull(dr.documentUniqueId);
			assertNotNull(dr.repositoryUniqueId);
			assertNotNull(dr.mimeType);
			assertNotNull(dr.document);
		}
	}
		
		
}
