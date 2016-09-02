package gov.ca.emsa.service;

import java.util.ArrayList;

import gov.ca.emsa.pulse.common.domain.PatientSearch;
import gov.ca.emsa.xcpd.XcpdUtils;
import gov.ca.emsa.xcpd.aqr.Slot;
import gov.ca.emsa.xcpd.prpa.cap.qbp.ParameterList;
import gov.ca.emsa.xcpd.rds.RetrieveDocumentSetRequest;
import gov.ca.emsa.xcpd.soap.DiscoveryRequestSoapEnvelope;
import gov.ca.emsa.xcpd.soap.DiscoveryResponseSoapEnvelope;
import gov.ca.emsa.xcpd.soap.QueryRequestSoapEnvelope;
import gov.ca.emsa.xcpd.soap.QueryResponseSoapEnvelope;
import gov.ca.emsa.xcpd.soap.RetrieveDocumentSetRequestSoapEnvelope;
import gov.ca.emsa.xcpd.soap.RetrieveDocumentSetResponseSoapEnvelope;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class EHealthQueryConsumerService {
	
	private static final Logger logger = LogManager.getLogger(EHealthQueryConsumerService.class);
	
	@RequestMapping(value = "/patientDiscovery", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
	public DiscoveryResponseSoapEnvelope patientDiscovery(@RequestBody DiscoveryRequestSoapEnvelope dr) throws JsonProcessingException{
		PatientSearch patientSearchTerms = new PatientSearch();
		ParameterList parameterList = dr.sBody.PRPA_IN201305UV02.controlActProcess.getQueryByParameter().parameterList;
		patientSearchTerms.setDob(parameterList.getLivingSubjectBirthTime().value.value);
		patientSearchTerms.setGender(parameterList.getLivingSubjectAdministrativeGender().value.code);
		patientSearchTerms.setGivenName(parameterList.getLivingSubjectName().getValue().given);
		patientSearchTerms.setFamilyName(parameterList.getLivingSubjectName().getValue().family);
		
		return XcpdUtils.generateDiscoveryResponse(patientSearchTerms.getGivenName(), patientSearchTerms.getFamilyName());
		
	}
	
	@RequestMapping(value = "/queryRequest", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
	public QueryResponseSoapEnvelope queryRequest(@RequestBody QueryRequestSoapEnvelope qr) throws JsonProcessingException{
		//patient id
		String patientId = qr.body.adhocQueryRequest.adhocQuery.id;
		//parameters for the documents to return
		ArrayList<Slot> slots = qr.body.adhocQueryRequest.adhocQuery.slots;
		
		return XcpdUtils.generateQueryResponse();
	}
	
	@RequestMapping(value = "/documentRequest", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
	public RetrieveDocumentSetResponseSoapEnvelope documentRequest(@RequestBody RetrieveDocumentSetRequestSoapEnvelope rds) throws JsonProcessingException{
		//patient id
		RetrieveDocumentSetRequest repositoryId = rds.body.documentSetRequest;
		
		return XcpdUtils.generateDocumentResponse();
	}

}
