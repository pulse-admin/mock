package gov.ca.emsa.xcpd.soap;

import gov.ca.emsa.xcpd.soap.header.DiscoveryRequestSoapHeader;
import gov.ca.emsa.xcpd.soap.header.DiscoveryResponseSoapHeader;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Envelope", namespace="http://www.w3.org/2003/05/soap-envelope")
public class DiscoveryRequestSoapEnvelope {
	
	@XmlAttribute(name = "xmlns:xsi") public String xmlnsxsi;
	@XmlAttribute(name = "xmlns:xcpd") public String xmlnsxcpd;
	@XmlAttribute(name = "xmlns:s") public String xsixmlnss;
	@XmlAttribute(name = "xmlns:a") public String itsVersion;
	@XmlAttribute(name = "xmlns:schemaLocation") public String schemaLocation;
	
	@XmlElement(name = "Header", namespace="http://www.w3.org/2003/05/soap-envelope") public DiscoveryRequestSoapHeader sHeader;
	
	@XmlElement(name = "Body", namespace="http://www.w3.org/2003/05/soap-envelope") public DiscoveryRequestSoapBody sBody;

}
