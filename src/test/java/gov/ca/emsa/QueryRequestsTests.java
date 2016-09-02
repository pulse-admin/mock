package gov.ca.emsa;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import gov.ca.emsa.xcpd.aqr.AdhocQuery;
import gov.ca.emsa.xcpd.aqr.Classification;
import gov.ca.emsa.xcpd.aqr.ExternalIdentifier;
import gov.ca.emsa.xcpd.aqr.ExtrinsicObject;
import gov.ca.emsa.xcpd.aqr.ObjectRef;
import gov.ca.emsa.xcpd.aqr.Slot;
import gov.ca.emsa.xcpd.aqr.ValueList;
import gov.ca.emsa.xcpd.soap.DiscoveryRequestSoapEnvelope;
import gov.ca.emsa.xcpd.soap.QueryRequestSoapEnvelope;
import gov.ca.emsa.xcpd.soap.QueryResponseSoapEnvelope;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

public class QueryRequestsTests {
	
	public static final String VALID_QUERY_REQUEST_FILE = "ValidStoredQueryRequest.xml";
	public static final String VALID_QUERY_RESPONSE_FILE = "ValidStoredQueryResponse.xml";
	
	//@Test
	public void mapRequestToObjectBody() throws JAXBException{
		Unmarshaller unmarshaller = JAXBContext.newInstance(QueryRequestSoapEnvelope.class).createUnmarshaller();
		ClassLoader classLoader = getClass().getClassLoader();
		QueryRequestSoapEnvelope qr = (QueryRequestSoapEnvelope)
				unmarshaller.unmarshal(classLoader.getResource(VALID_QUERY_REQUEST_FILE));
		
		//check all SOAP header elements are there
		assertNotNull(qr.header);
		assertNotNull(qr.header.action);
		assertNotNull(qr.header.action.mustUnderstand);
		assertNotNull(qr.header.messageId);
		assertNotNull(qr.header.replyTo.address);
		
		assertNotNull(qr.body);
		assertNotNull(qr.body.adhocQueryRequest);
		assertNotNull(qr.body.adhocQueryRequest.responseOption);
		assertNotNull(qr.body.adhocQueryRequest.responseOption.returnComposedObjects);
		assertNotNull(qr.body.adhocQueryRequest.responseOption.returnType);
		assertNotNull(qr.body.adhocQueryRequest.adhocQuery);
		AdhocQuery aq = qr.body.adhocQueryRequest.adhocQuery;
		for(Slot slot : aq.slots){
			assertNotNull(slot);
			assertNotNull(slot.valueList);
			assertNotNull(slot.valueList.value);
		}
	}
	
	@Test
	public void mapResponseToObjectBody() throws JAXBException{
		Unmarshaller unmarshaller = JAXBContext.newInstance(QueryResponseSoapEnvelope.class).createUnmarshaller();
		ClassLoader classLoader = getClass().getClassLoader();
		QueryResponseSoapEnvelope qr = (QueryResponseSoapEnvelope)
				unmarshaller.unmarshal(classLoader.getResource(VALID_QUERY_RESPONSE_FILE));
		
		//check all SOAP header elements are there
		assertNotNull(qr.header);
		assertNotNull(qr.header.action);
		assertNotNull(qr.header.action.mustUnderstand);
		assertNotNull(qr.header.messageId);
		assertNotNull(qr.header.relatesTo);
		assertNotNull(qr.header.to);
		
		assertNotNull(qr.body);
		assertNotNull(qr.body.adhocQueryResponse);
		assertNotNull(qr.body.adhocQueryResponse.registryObjectList);
		assertNotNull(qr.body.adhocQueryResponse.registryObjectList.extrinsicObject);
		assertNotNull(qr.body.adhocQueryResponse.registryObjectList.extrinsicObject.slots);
		ExtrinsicObject eo = qr.body.adhocQueryResponse.registryObjectList.extrinsicObject;
		for(Slot slot : eo.slots){
			assertNotNull(slot);
			assertNotNull(slot.valueList);
			ValueList vl = slot.valueList;
			for(String v : vl.value){
				assertNotNull(v);
			}
		}
		assertNotNull(qr.body.adhocQueryResponse.registryObjectList.extrinsicObject.name);
		assertNotNull(qr.body.adhocQueryResponse.registryObjectList.extrinsicObject.description);
		assertNotNull(qr.body.adhocQueryResponse.registryObjectList.extrinsicObject.classification);
		for(Classification classification : eo.classification){
			assertNotNull(classification.slot);
			assertNotNull(classification.name);
			assertNotNull(classification.name.localizedString);
		}
		assertNotNull(qr.body.adhocQueryResponse.registryObjectList.extrinsicObject.externalIdentifier);
		for(ExternalIdentifier external : eo.externalIdentifier){
			assertNotNull(external.name);
			assertNotNull(external.name.localizedString);
			assertNotNull(external.description);
		}
		for(ObjectRef or : qr.body.adhocQueryResponse.registryObjectList.objectRef){
			assertNotNull(or.id);
		}
	}

}
