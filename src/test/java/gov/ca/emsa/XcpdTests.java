package gov.ca.emsa;

import static org.junit.Assert.*;

import java.io.File;

import gov.ca.emsa.xcpd.soap.DiscoveryRequestSoapEnvelope;
import gov.ca.emsa.xcpd.soap.DiscoveryResponseSoapEnvelope;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

public class XcpdTests {
	
	public static final String VALID_XCPD_REQUEST_FILE = "ValidXcpdRequest.xml";
	
	public static final String VALID_XCPD_RESPONSE_FILE = "ValidXcpdResponse.xml";
	
	@Test
	public void mapRequestToObjectBody() throws JAXBException{
		Unmarshaller unmarshaller = JAXBContext.newInstance(DiscoveryRequestSoapEnvelope.class).createUnmarshaller();
		ClassLoader classLoader = getClass().getClassLoader();
		DiscoveryRequestSoapEnvelope drsp = (DiscoveryRequestSoapEnvelope)
				unmarshaller.unmarshal(classLoader.getResource(VALID_XCPD_REQUEST_FILE));
		
		//check all SOAP header elements are there
		assertNotNull(drsp.sHeader);
		assertNotNull(drsp.sHeader.action);
		assertNotNull(drsp.sHeader.action.mustUnderstand);
		assertNotNull(drsp.sHeader.messageId);
		assertNotNull(drsp.sHeader.replyTo.address);
		assertNotNull(drsp.sHeader.cttl);
		
		
		assertNotNull(drsp.sBody);
		assertNotNull(drsp.sBody.PRPA_IN201305UV02);
		assertNotNull(drsp.sBody.PRPA_IN201305UV02.id);
		assertNotNull(drsp.sBody.PRPA_IN201305UV02.creationTime);
		assertNotNull(drsp.sBody.PRPA_IN201305UV02.interactionId);
		assertNotNull(drsp.sBody.PRPA_IN201305UV02.processingCode);
		assertNotNull(drsp.sBody.PRPA_IN201305UV02.processingModeCode);
		
		assertNotNull(drsp.sBody.PRPA_IN201305UV02.receiver);
		assertNotNull(drsp.sBody.PRPA_IN201305UV02.receiver.device);
		assertNotNull(drsp.sBody.PRPA_IN201305UV02.receiver.device.id);
		assertNotNull(drsp.sBody.PRPA_IN201305UV02.receiver.device.telecom);
		
		assertNotNull(drsp.sBody.PRPA_IN201305UV02.sender);
		assertNotNull(drsp.sBody.PRPA_IN201305UV02.sender.device);
		assertNotNull(drsp.sBody.PRPA_IN201305UV02.sender.device.id);
		assertNotNull(drsp.sBody.PRPA_IN201305UV02.sender.device.asAgent);
		assertNotNull(drsp.sBody.PRPA_IN201305UV02.sender.device.asAgent.representedOrganization);
		assertNotNull(drsp.sBody.PRPA_IN201305UV02.sender.device.asAgent.representedOrganization.id);
		
		assertNotNull(drsp.sBody.PRPA_IN201305UV02.controlActProcess);
		assertNotNull(drsp.sBody.PRPA_IN201305UV02.controlActProcess.classCode);
		assertNotNull(drsp.sBody.PRPA_IN201305UV02.controlActProcess.moodCode);
		assertNotNull(drsp.sBody.PRPA_IN201305UV02.controlActProcess.getCode());
		assertNotNull(drsp.sBody.PRPA_IN201305UV02.controlActProcess.getQueryByParameter());
		assertNotNull(drsp.sBody.PRPA_IN201305UV02.controlActProcess.getQueryByParameter().queryId.root);
		assertNotNull(drsp.sBody.PRPA_IN201305UV02.controlActProcess.getQueryByParameter().queryId.extension);
		assertNotNull(drsp.sBody.PRPA_IN201305UV02.controlActProcess.getQueryByParameter().getStatusCode().code);
		assertNotNull(drsp.sBody.PRPA_IN201305UV02.controlActProcess.getQueryByParameter().responsePriorityCode.code);
		assertNotNull(drsp.sBody.PRPA_IN201305UV02.controlActProcess.getQueryByParameter().matchCriterionList.minimumDegreeMatch.value);
		assertNotNull(drsp.sBody.PRPA_IN201305UV02.controlActProcess.getQueryByParameter().matchCriterionList.minimumDegreeMatch.semanticsText);
		assertNotNull(drsp.sBody.PRPA_IN201305UV02.controlActProcess.getQueryByParameter().parameterList);
		assertNotNull(drsp.sBody.PRPA_IN201305UV02.controlActProcess.getQueryByParameter().parameterList.getLivingSubjectAdministrativeGender().value.code);
		assertNotNull(drsp.sBody.PRPA_IN201305UV02.controlActProcess.getQueryByParameter().parameterList.getLivingSubjectAdministrativeGender().semanticsText);
		assertNotNull(drsp.sBody.PRPA_IN201305UV02.controlActProcess.getQueryByParameter().parameterList.getLivingSubjectBirthTime().value.value);
		assertNotNull(drsp.sBody.PRPA_IN201305UV02.controlActProcess.getQueryByParameter().parameterList.getLivingSubjectBirthTime().semanticsText);
		assertNotNull(drsp.sBody.PRPA_IN201305UV02.controlActProcess.getQueryByParameter().parameterList.getLivingSubjectName().getValue().family);
		assertNotNull(drsp.sBody.PRPA_IN201305UV02.controlActProcess.getQueryByParameter().parameterList.getLivingSubjectName().getValue().given);
		assertNotNull(drsp.sBody.PRPA_IN201305UV02.controlActProcess.getQueryByParameter().parameterList.livingSubjectId.get(0).getLsiv().root);
		assertNotNull(drsp.sBody.PRPA_IN201305UV02.controlActProcess.getQueryByParameter().parameterList.livingSubjectId.get(0).getLsiv().extension);
		assertNotNull(drsp.sBody.PRPA_IN201305UV02.controlActProcess.getQueryByParameter().parameterList.livingSubjectId.get(0).semanticsText);
		assertNotNull(drsp.sBody.PRPA_IN201305UV02.controlActProcess.getQueryByParameter().parameterList.livingSubjectId.get(1).getLsiv().root);
		assertNotNull(drsp.sBody.PRPA_IN201305UV02.controlActProcess.getQueryByParameter().parameterList.livingSubjectId.get(1).getLsiv().extension);
		assertNotNull(drsp.sBody.PRPA_IN201305UV02.controlActProcess.getQueryByParameter().parameterList.livingSubjectId.get(1).semanticsText);
	}
	
	@Test
	public void mapResponseToObjectBody() throws JAXBException{
		Unmarshaller unmarshaller = JAXBContext.newInstance(DiscoveryResponseSoapEnvelope.class).createUnmarshaller();
		ClassLoader classLoader = getClass().getClassLoader();
		DiscoveryResponseSoapEnvelope drsp = (DiscoveryResponseSoapEnvelope)
				unmarshaller.unmarshal(classLoader.getResource(VALID_XCPD_RESPONSE_FILE));
		
		//check all SOAP header elements are there
		assertNotNull(drsp.sHeader);
		assertNotNull(drsp.sHeader.action);
		assertNotNull(drsp.sHeader.action.mustUnderstand);
		assertNotNull(drsp.sHeader.cttl);
		
		
		assertNotNull(drsp.sBody);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.id);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.creationTime);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.interactionId);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.processingCode);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.processingModeCode);
		
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.receiver);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.receiver.device);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.receiver.device.id);
		
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.sender);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.sender.device);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.sender.device.id);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.sender.device.telecom);
		
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.classCode);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.moodCode);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.getCode());
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.getCode().code);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.getCode().codeSystem);
		
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.getQueryAck().getQueryId().root);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.getQueryAck().getQueryId().extension);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.getQueryAck().getQueryResponseCode().code);
		
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.subjects);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.subjects.get(0).typeCode);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.subjects.get(0).registrationEvent.classCode);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.subjects.get(0).registrationEvent.moodCode);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.subjects.get(0).registrationEvent.getId());
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.subjects.get(0).registrationEvent.getStatusCode());
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.subjects.get(0).registrationEvent.getSubject1());
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.subjects.get(0).registrationEvent.getSubject1().typeCode);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.subjects.get(0).registrationEvent.getSubject1().getPatient().classCode);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.subjects.get(0).registrationEvent.getSubject1().getPatient().getId().root);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.subjects.get(0).registrationEvent.getSubject1().getPatient().getId().extension);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.subjects.get(0).registrationEvent.getSubject1().getPatient().getStatusCode().code);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.subjects.get(0).registrationEvent.getSubject1().getPatient().getPatientPerson().getName().family);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.subjects.get(0).registrationEvent.getSubject1().getPatient().getPatientPerson().getName().given);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.subjects.get(0).registrationEvent.getSubject1().getPatient().getPatientPerson().telecoms);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.subjects.get(0).registrationEvent.getSubject1().getPatient().getPatientPerson().addr);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.subjects.get(0).registrationEvent.getSubject1().getPatient().getPatientPerson().addr.streetAddressLine);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.subjects.get(0).registrationEvent.getSubject1().getPatient().getPatientPerson().addr.city);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.subjects.get(0).registrationEvent.getSubject1().getPatient().getPatientPerson().addr.state);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.subjects.get(0).registrationEvent.getSubject1().getPatient().getPatientPerson().asOtherIDs.get(0).classCode);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.subjects.get(0).registrationEvent.getSubject1().getPatient().getPatientPerson().asOtherIDs.get(0).id);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.subjects.get(0).registrationEvent.getSubject1().getPatient().getPatientPerson().asOtherIDs.get(0).scopingOrganization.classCode);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.subjects.get(0).registrationEvent.getSubject1().getPatient().getPatientPerson().asOtherIDs.get(0).scopingOrganization.determinerCode);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.subjects.get(0).registrationEvent.getSubject1().getPatient().getPatientPerson().asOtherIDs.get(0).scopingOrganization.id);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.subjects.get(0).registrationEvent.getSubject1().getPatient().getPatientPerson().asOtherIDs.get(0).scopingOrganization.classCode);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.subjects.get(0).registrationEvent.getSubject1().getPatient().getProviderOrganization().id);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.subjects.get(0).registrationEvent.getSubject1().getPatient().getProviderOrganization().name);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.subjects.get(0).registrationEvent.getSubject1().getPatient().getProviderOrganization().contactParty);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.subjects.get(0).registrationEvent.getSubject1().getPatient().getProviderOrganization().contactParty.telecom);
		//assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.getSubjects().get(0).registrationEvent.getSubject1().getPatient().getPatientPerson().);
								
		
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.getQueryByParameter());
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.getQueryByParameter().queryId.root);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.getQueryByParameter().queryId.extension);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.getQueryByParameter().getStatusCode().code);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.getQueryByParameter().parameterList);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.getQueryByParameter().parameterList.getLivingSubjectAdministrativeGender().value.code);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.getQueryByParameter().parameterList.getLivingSubjectAdministrativeGender().semanticsText);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.getQueryByParameter().parameterList.getLivingSubjectBirthTime().value.value);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.getQueryByParameter().parameterList.getLivingSubjectBirthTime().semanticsText);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.getQueryByParameter().parameterList.getLivingSubjectName().getValue().family);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.getQueryByParameter().parameterList.getLivingSubjectName().getValue().given);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.getQueryByParameter().parameterList.livingSubjectId.get(0).getLsiv().root);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.getQueryByParameter().parameterList.livingSubjectId.get(0).getLsiv().extension);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.getQueryByParameter().parameterList.livingSubjectId.get(0).semanticsText);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.getQueryByParameter().parameterList.livingSubjectId.get(1).getLsiv().root);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.getQueryByParameter().parameterList.livingSubjectId.get(1).getLsiv().extension);
		assertNotNull(drsp.sBody.PRPA_IN201306UV02.controlActProcess.getQueryByParameter().parameterList.livingSubjectId.get(1).semanticsText);
	}

}
