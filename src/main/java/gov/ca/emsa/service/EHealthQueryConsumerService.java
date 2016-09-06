package gov.ca.emsa.service;

import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;

import org.hl7.v3.PRPAIN201305UV02;

public interface EHealthQueryConsumerService {
	public PRPAIN201305UV02 unMarshallPatientDiscoveryRequestObject(String xml);
	public AdhocQueryRequest unMarshallDocumentQueryRequestObject(String xml);
	public RetrieveDocumentSetRequestType unMarshallDocumentSetRetrieveRequestObject(String xml);
}
