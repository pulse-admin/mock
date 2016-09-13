package gov.ca.emsa.service;

import javax.xml.soap.SOAPException;

import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;

import org.hl7.v3.PRPAIN201305UV02;
import org.opensaml.common.SAMLException;

public interface EHealthQueryConsumerService {
	public PRPAIN201305UV02 unMarshallPatientDiscoveryRequestObject(String xml) throws SOAPException, SAMLException;
	public AdhocQueryRequest unMarshallDocumentQueryRequestObject(String xml) throws SAMLException;
	public RetrieveDocumentSetRequestType unMarshallDocumentSetRetrieveRequestObject(String xml) throws SAMLException;
	public String createSOAPFault();
}