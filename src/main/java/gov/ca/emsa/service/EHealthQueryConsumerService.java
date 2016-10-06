package gov.ca.emsa.service;

import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPException;

import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.opensaml.common.SAMLException;

public interface EHealthQueryConsumerService {
	public PRPAIN201305UV02 unMarshallPatientDiscoveryRequestObject(String xml) throws SOAPException, SAMLException;
	public AdhocQueryRequest unMarshallDocumentQueryRequestObject(String xml) throws SAMLException;
	public AdhocQueryResponse unMarshallDocumentQueryResponseObject(String xml) throws SAMLException,SOAPException ;
	public RetrieveDocumentSetRequestType unMarshallDocumentSetRetrieveRequestObject(String xml) throws SAMLException;
	public PRPAIN201306UV02 unMarshallPatientDiscoveryResponseObject(String xml) throws SOAPException, SAMLException;
	public String marshallPatientDiscoveryResponse(PRPAIN201306UV02 response) throws JAXBException;
	public String marshallDocumentQueryResponse(AdhocQueryResponse response) throws JAXBException;
	public String marshallDocumentSetResponseType(RetrieveDocumentSetResponseType response) throws JAXBException;
	public RetrieveDocumentSetResponseType unMarshallDocumentSetRetrieveResponseObject(String xml);
	public String createSOAPFault();
}