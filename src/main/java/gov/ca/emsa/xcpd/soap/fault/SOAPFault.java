package gov.ca.emsa.xcpd.soap.fault;

import javax.xml.bind.annotation.XmlElement;

public class SOAPFault {
	
	@XmlElement public String faultcode;
	@XmlElement public String faultString;

}
