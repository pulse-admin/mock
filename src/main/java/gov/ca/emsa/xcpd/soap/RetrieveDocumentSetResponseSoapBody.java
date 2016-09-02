package gov.ca.emsa.xcpd.soap;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import gov.ca.emsa.xcpd.aqr.AdhocQueryResponse;
import gov.ca.emsa.xcpd.rds.RetrieveDocumentSetResponse;
import gov.ca.emsa.xcpd.soap.fault.SOAPFault;

public class RetrieveDocumentSetResponseSoapBody {
	@XmlElement(name = "RetrieveDocumentSetResponse") public RetrieveDocumentSetResponse retrieveDocumentSetResponse;
	
	@XmlElement(name = "Fault", namespace = "http://www.w3.org/2003/05/soap-envelope") public SOAPFault fault;
}
