package gov.ca.emsa.xcpd.soap;

import javax.xml.bind.annotation.XmlElement;

import gov.ca.emsa.xcpd.PatientDiscoveryResponse;
import gov.ca.emsa.xcpd.soap.fault.SOAPFault;

public class DiscoveryResponseSoapBody {
	
	public PatientDiscoveryResponse PRPA_IN201306UV02;
	
	@XmlElement(name = "Fault", namespace = "http://www.w3.org/2003/05/soap-envelope") public SOAPFault fault;

}
